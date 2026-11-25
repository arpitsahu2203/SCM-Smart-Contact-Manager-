package org.arpitsahu.smc.ServiceImpl;

import org.arpitsahu.smc.Entities.*;
import org.arpitsahu.smc.Helper.*;
import org.arpitsahu.smc.Repository.*;
import org.arpitsahu.smc.Services.*;
import org.arpitsahu.smc.forms.EmailSendForm;
import org.springframework.core.io.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class EmailServiceImpl implements EmailService {
    private final SentEmailRepo emails;
    private final EmailAttachmentRepo attachments;
    private final contactRepo contacts;
    private final UserRepo users;
    private final GoogleOAuthTokenHelper tokens;
    private final GmailApiSenderService gmail;

    public EmailServiceImpl(SentEmailRepo e, EmailAttachmentRepo a, contactRepo c, UserRepo u, GoogleOAuthTokenHelper t,
            GmailApiSenderService g) {
        emails = e;
        attachments = a;
        contacts = c;
        users = u;
        tokens = t;
        gmail = g;
    }

    public SentEmail send(EmailSendForm form, Authentication auth) {
        Users user = currentUser(auth);
        Contact contact = contacts.findById(form.getContactId())
                .orElseThrow(() -> new IllegalArgumentException("Contact not found"));
        if (!contact.getUser().getId().equals(user.getId()))
            throw new IllegalArgumentException("Contact access denied");
        if (!tokens.isGoogleAccountConnected(auth))
            throw new IllegalStateException("Sign in with Google to enable Gmail sending.");
        List<MultipartFile> files = form.getAttachments() == null ? List.of()
                : form.getAttachments().stream().filter(f -> f != null && !f.isEmpty()).toList();
        long bytes = files.stream().mapToLong(MultipartFile::getSize).sum();
        if (bytes > 25L * 1024 * 1024)
            throw new IllegalArgumentException("Attachments must total less than 25 MB");
        SentEmail email = SentEmail.builder().id(UUID.randomUUID().toString()).user(user).contact(contact)
                .recipientEmail(form.getRecipientEmail()).cc(form.getCc()).bcc(form.getBcc()).subject(form.getSubject())
                .body(form.getBody()).sentAt(LocalDateTime.now()).status(EmailStatus.FAILED).build();
        emails.save(email);
        try {
            saveFiles(email, files);
            gmail.send(user.getEmail(), form.getRecipientEmail(), form.getCc(), form.getBcc(), form.getSubject(),
                    form.getBody(), files, tokens.accessToken(auth));
            email.setStatus(EmailStatus.SENT);
            email.setErrorMessage(null);
        } catch (Exception ex) {
            email.setStatus(EmailStatus.FAILED);
            email.setErrorMessage(safeErrorMessage(ex));
        }
        return emails.save(email);
    }

    /** Keeps provider diagnostics useful without exceeding the database VARCHAR(2000) column. */
    private String safeErrorMessage(Exception exception) {
        if (exception instanceof GmailApiSenderService.GmailApiException gmailError) {
            return "GMAIL_API|" + gmailError.getStatusCode() + "|" + gmailError.getReason();
        }
        String message = exception.getMessage();
        if (message == null || message.isBlank()) {
            return "Gmail dispatch failed. Please reconnect Google and try again.";
        }
        final int maximumLength = 1800;
        return message.length() <= maximumLength ? message
                : message.substring(0, maximumLength) + "… (error details truncated)";
    }

    public String sendFailureMessage(SentEmail email) {
        String error = email.getErrorMessage() == null ? "" : email.getErrorMessage();
        if (error.startsWith("GMAIL_API|")) {
            String[] details = error.split("\\|", 3);
            int status = details.length > 1 ? parseStatus(details[1]) : 0;
            String reason = details.length > 2 ? details[2].toLowerCase() : "unknown";
            return gmailFailureMessage(status, reason);
        }

        // Backward-compatible handling for emails sent before structured diagnostics existed.
        String legacyError = error.toLowerCase();
        if (legacyError.contains("401") || legacyError.contains("unauthorized") || legacyError.contains("invalid credentials")
                || legacyError.contains("token")) {
            return "Your Google session has expired. Sign in with Google again, then resend the email.";
        }
        if (legacyError.contains("access not configured") || legacyError.contains("service_disabled")) {
            return "The Gmail API is not enabled for this Google Cloud project. Enable the Gmail API, then try again.";
        }
        if (legacyError.contains("insufficient permission") || legacyError.contains("gmail.send")) {
            return "Google did not grant Gmail sending access. Reconnect Google and approve the Gmail sending permission, then try again.";
        }
        if (legacyError.contains("recipient") || legacyError.contains("address")) {
            return "Gmail could not use the recipient address. Check the email address and try again.";
        }
        return "Gmail could not send this email. Reconnect Google and try again.";
    }

    private int parseStatus(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private String gmailFailureMessage(int status, String reason) {
        if (status == 401) {
            return "Your Google session has expired. Sign in with Google again, then resend the email.";
        }
        if (status == 403 && (reason.contains("accessnotconfigured") || reason.contains("service_disabled"))) {
            return "The Gmail API is not enabled for this Google Cloud project. Enable the Gmail API, then try again.";
        }
        if (status == 403 && (reason.contains("insufficientpermissions") || reason.contains("permission_denied"))) {
            return "Google did not grant Gmail sending access. Reconnect Google and approve the Gmail sending permission, then try again.";
        }
        if (status == 403 && (reason.contains("userratelimitexceeded") || reason.contains("ratelimitexceeded")
                || reason.contains("dailylimitexceeded"))) {
            return "Gmail's sending limit has been reached. Wait and try again later.";
        }
        if (status == 400) {
            return "Gmail rejected this message. Check the recipient addresses, subject, and attachments, then try again.";
        }
        if (status == 429) {
            return "Gmail is temporarily rate-limiting requests. Wait a few minutes, then try again.";
        }
        if (status >= 500 && status < 600) {
            return "Gmail is temporarily unavailable. Please try sending again shortly.";
        }
        return "Google rejected the Gmail request. Check the Gmail API and Google account access, then try again.";
    }

    private void saveFiles(SentEmail email, List<MultipartFile> files) throws IOException {
        Path dir = Paths.get("uploads", "email_attachments", email.getId()).toAbsolutePath().normalize();
        Files.createDirectories(dir);
        for (MultipartFile f : files) {
            String name = Paths.get(Objects.requireNonNullElse(f.getOriginalFilename(), "attachment")).getFileName()
                    .toString();
            Path path = dir.resolve(UUID.randomUUID() + "_" + name);
            Files.copy(f.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            EmailAttachment a = EmailAttachment.builder().id(UUID.randomUUID().toString()).sentEmail(email)
                    .originalFileName(name)
                    .fileType(Objects.requireNonNullElse(f.getContentType(), "application/octet-stream"))
                    .fileSize(f.getSize()).storagePath(path.toString()).build();
            attachments.save(a);
            email.getAttachments().add(a);
        }
    }

    public List<SentEmail> history(String contactId, Authentication auth) {
        Users u = currentUser(auth);
        Contact c = contacts.findById(contactId).orElseThrow(() -> new IllegalArgumentException("Contact not found"));
        if (!c.getUser().getId().equals(u.getId()))
            throw new IllegalArgumentException("Contact access denied");
        return emails.findByContactAndUserOrderBySentAtDesc(c, u);
    }

    public Resource loadAttachmentAsResource(String id, Authentication auth) {
        EmailAttachment a = attachments.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attachment not found"));
        if (!a.getSentEmail().getUser().getId().equals(currentUser(auth).getId()))
            throw new IllegalArgumentException("Attachment access denied");
        return new FileSystemResource(a.getStoragePath());
    }

    private Users currentUser(Authentication a) {
        return users.findByEmail(Helper.getEmailOfLoggedInUser(a))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
