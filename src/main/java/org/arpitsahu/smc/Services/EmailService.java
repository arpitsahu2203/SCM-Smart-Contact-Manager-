package org.arpitsahu.smc.Services;

import org.arpitsahu.smc.Entities.*;
import org.arpitsahu.smc.forms.EmailSendForm;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import java.util.*;

public interface EmailService {
    SentEmail send(EmailSendForm form, Authentication authentication);

    List<SentEmail> history(String contactId, Authentication authentication);

    Resource loadAttachmentAsResource(String attachmentId, Authentication authentication);
}
