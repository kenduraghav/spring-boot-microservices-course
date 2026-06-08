package com.example.bookstore.webapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class SecurityHelper {

    private static final Logger log = LoggerFactory.getLogger(SecurityHelper.class);

    private final OAuth2AuthorizedClientService authorizedClientService;

    public SecurityHelper(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    public String getAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof OAuth2AuthenticationToken authToken)) {
            return null;
        }

        //        OAuth2AuthorizedClient authorizedClient =
        // authorizedClientService.loadAuthorizedClient(authToken.getAuthorizedClientRegistrationId(),
        // authToken.getName());
        //
        //        if (authorizedClient == null || authorizedClient.getAccessToken() == null) {
        //            return null;
        //        }
        //        log.info("accessToken : " + authorizedClient.getAccessToken().getTokenValue());

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        return oidcUser.getIdToken().getTokenValue();
    }
}
