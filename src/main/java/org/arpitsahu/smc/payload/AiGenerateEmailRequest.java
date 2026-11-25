package org.arpitsahu.smc.payload;

import lombok.*;

@Getter
@Setter
public class AiGenerateEmailRequest {
    private String contactName, contactDescription, purpose, tone, keyPoints, callToAction;
}
