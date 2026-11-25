package org.arpitsahu.smc.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SentEmail {
    @Id
    private String id;
    @ManyToOne
    @JsonIgnore
    private Users user;
    @ManyToOne
    @JsonIgnore
    private Contact contact;
    private String recipientEmail;
    @Column(name = "cc_recipients")
    private String cc;
    @Column(name = "bcc_recipients")
    private String bcc;
    private String subject;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String body;
    @Enumerated(EnumType.STRING)
    private EmailStatus status;
    @Column(length = 2000)
    private String errorMessage;
    private LocalDateTime sentAt;
    @OneToMany(mappedBy = "sentEmail", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EmailAttachment> attachments = new ArrayList<>();
}
