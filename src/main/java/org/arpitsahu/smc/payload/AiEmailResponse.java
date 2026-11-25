package org.arpitsahu.smc.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiEmailResponse {
    private String subject, body, improvementNotes;
}
