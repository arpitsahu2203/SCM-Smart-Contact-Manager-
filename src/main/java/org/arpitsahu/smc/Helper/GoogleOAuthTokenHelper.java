package org.arpitsahu.smc.Helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class GoogleOAuthTokenHelper {
    private final OAuth2AuthorizedClientService clients;

    public GoogleOAuthTokenHelper(OAuth2AuthorizedClientService clients) {
        this.clients = clients;
    }

    public String accessToken(Authentication auth) {
        if (!(auth instanceof OAuth2AuthenticationToken token)
                || !"google".equals(token.getAuthorizedClientRegistrationId()))
            return null;
        OAuth2AuthorizedClient client = clients.loadAuthorizedClient("google", token.getName());
        return client != null && client.getAccessToken() != null ? client.getAccessToken().getTokenValue() : null;
    }

    public boolean isGoogleAccountConnected(Authentication auth) {
        return accessToken(auth) != null;
    }
}
