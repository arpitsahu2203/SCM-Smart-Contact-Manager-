package org.arpitsahu.smc.Services;

import org.arpitsahu.smc.payload.*;

public interface AiEmailService {
    AiEmailResponse generate(AiGenerateEmailRequest request);

    AiEmailResponse refine(AiRefineEmailRequest request);
}
