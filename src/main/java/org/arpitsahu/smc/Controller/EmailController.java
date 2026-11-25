package org.arpitsahu.smc.Controller;

import jakarta.servlet.http.HttpSession;
import org.arpitsahu.smc.*;
import org.arpitsahu.smc.Entities.*;
import org.arpitsahu.smc.Repository.EmailAttachmentRepo;
import org.arpitsahu.smc.ServiceImpl.EmailServiceImpl;
import org.arpitsahu.smc.Services.EmailService;
import org.arpitsahu.smc.forms.EmailSendForm;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/SMC/user/email")
public class EmailController {
    private final EmailService emails;
    private final EmailAttachmentRepo attachments;

    public EmailController(EmailService e, EmailAttachmentRepo a) {
        emails = e;
        attachments = a;
    }

    @PostMapping("/send")
    public String send(@ModelAttribute EmailSendForm form, Authentication a, HttpSession s) {
        try {
            SentEmail result = emails.send(form, a);
            String content = result.getStatus() == EmailStatus.SENT ? "Email sent via Gmail."
                    : emails instanceof EmailServiceImpl impl ? impl.sendFailureMessage(result)
                            : "Gmail could not send this email. Reconnect Google and try again.";
            s.setAttribute("message",
                    org.arpitsahu.smc.Helper.messageHelper.builder()
                            .type(result.getStatus() == EmailStatus.SENT ? org.arpitsahu.smc.Helper.messageEnum.green
                                    : org.arpitsahu.smc.Helper.messageEnum.red)
                            .content(content).build());
        } catch (Exception exception) {
            s.setAttribute("message", org.arpitsahu.smc.Helper.messageHelper.builder()
                    .type(org.arpitsahu.smc.Helper.messageEnum.red)
                    .content(
                            "Unable to send the email. Please check the recipient and Google connection, then try again.")
                    .build());
        }
        return "redirect:/SMC/user/Contact/view";
    }

    @GetMapping("/attachment/{id}")
    public ResponseEntity<Resource> attachment(@PathVariable String id, Authentication a) {
        EmailAttachment file = attachments.findById(id).orElseThrow();
        Resource r = emails.loadAttachmentAsResource(id, a);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getOriginalFileName().replace("\"", "") + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM).body(r);
    }
}
