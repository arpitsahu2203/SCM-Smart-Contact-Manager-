package org.arpitsahu.smc.ServiceImpl;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.util.*;

@Service
public class GmailApiSenderService {

    private static final ObjectMapper JSON = new ObjectMapper();
    private final RestClient client;

    public GmailApiSenderService() {
        this.client = RestClient.builder().baseUrl("https://gmail.googleapis.com").build();
    }

    public void send(String from, String to, String cc, String bcc, String subject, String body, List<MultipartFile> files, String accessToken) throws Exception {
        Session session = Session.getInstance(new Properties());
        MimeMessage message = new MimeMessage(session);
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        helper.setTo(to);
        if (cc != null && !cc.isBlank()) helper.setCc(cc);
        if (bcc != null && !bcc.isBlank()) helper.setBcc(bcc);
        helper.setSubject(subject);
        helper.setText(toHtmlBody(body), true);

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                helper.addAttachment(
                        Objects.requireNonNullElse(file.getOriginalFilename(), "attachment"),
                        new ByteArrayResource(file.getBytes()),
                        Objects.requireNonNullElse(file.getContentType(), "application/octet-stream")
                );
            }
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        message.writeTo(out);
        String raw = Base64.getUrlEncoder().withoutPadding().encodeToString(out.toByteArray());

        try {
            client.post()
                    .uri("/gmail/v1/users/me/messages/send")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("raw", raw))
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException exception) {
            throw GmailApiException.from(exception);
        }
    }

    private String toHtmlBody(String body) {
        String escaped = body == null ? "" : body
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
        return escaped.replace("\r\n", "\n").replace("\r", "\n").replace("\n", "<br>\n");
    }

    /**
     * Keeps Gmail's HTTP status and machine-readable reason available to the service layer.
     * The raw provider response is deliberately not persisted or displayed to the user.
     */
    public static final class GmailApiException extends RuntimeException {
        private final int statusCode;
        private final String reason;

        private GmailApiException(int statusCode, String reason) {
            super("Gmail API request failed: " + statusCode + " (" + reason + ")");
            this.statusCode = statusCode;
            this.reason = reason;
        }

        static GmailApiException from(RestClientResponseException exception) {
            String reason = "unknown";
            try {
                JsonNode error = JSON.readTree(exception.getResponseBodyAsString()).path("error");
                JsonNode errors = error.path("errors");
                if (errors.isArray() && !errors.isEmpty()) {
                    reason = errors.get(0).path("reason").asText(reason);
                } else {
                    reason = error.path("status").asText(reason);
                }
            } catch (Exception ignored) {
                // A non-JSON provider response still has a useful HTTP status.
            }
            return new GmailApiException(exception.getStatusCode().value(), reason);
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getReason() {
            return reason;
        }
    }
}


