package org.arpitsahu.smc.Controller;

import org.arpitsahu.smc.Helper.*;
import org.arpitsahu.smc.Services.*;
import org.arpitsahu.smc.payload.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/SMC/api/email")
public class EmailApiController {
    private final GoogleOAuthTokenHelper google;
    private final AiEmailService ai;
    private final EmailService emails;

    public EmailApiController(GoogleOAuthTokenHelper g, AiEmailService a, EmailService e) {
        google = g;
        ai = a;
        emails = e;
    }

    @GetMapping("/status")
    public Map<String, Object> status(Authentication a) {
        return Map.of("googleConnected", google.isGoogleAccountConnected(a), "userEmail",
                Helper.getEmailOfLoggedInUser(a));
    }

    @PostMapping("/ai/generate")
    public AiEmailResponse generate(@RequestBody AiGenerateEmailRequest r) {
        return ai.generate(r);
    }

    @PostMapping("/ai/refine")
    public AiEmailResponse refine(@RequestBody AiRefineEmailRequest r) {
        return ai.refine(r);
    }

    @GetMapping("/history/{contactId}")
    public Object history(@PathVariable String contactId, Authentication a) {
        return emails.history(contactId, a);
    }
}
