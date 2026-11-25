package org.arpitsahu.smc.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailAttachment {
    @Id
    private String id;

    @ManyToOne
    @JsonIgnore
    private SentEmail sentEmail;

    private String originalFileName, fileType, storagePath;

    private long fileSize;
}
