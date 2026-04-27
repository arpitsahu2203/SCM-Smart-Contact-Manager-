package org.arpitsahu.smc.Helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class messageHelper {
    private String content;
    @Builder.Default
    private messageEnum type = messageEnum.blue;
}