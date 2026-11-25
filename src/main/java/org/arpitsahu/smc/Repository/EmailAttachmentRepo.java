package org.arpitsahu.smc.Repository;

import org.arpitsahu.smc.Entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface EmailAttachmentRepo extends JpaRepository<EmailAttachment, String> {
    List<EmailAttachment> findBySentEmail(SentEmail sentEmail);
}
