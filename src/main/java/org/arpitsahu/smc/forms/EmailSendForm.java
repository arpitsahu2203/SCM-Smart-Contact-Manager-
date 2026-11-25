package org.arpitsahu.smc.forms;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@Getter
@Setter
public class EmailSendForm {
    private String contactId, recipientEmail, subject, body, cc, bcc;
    private List<MultipartFile> attachments = new ArrayList<>();
}
