package org.arpitsahu.smc.payload;

import lombok.*;

@Getter
@Setter
public class AiRefineEmailRequest {
    private String currentSubject, currentBody;
}
