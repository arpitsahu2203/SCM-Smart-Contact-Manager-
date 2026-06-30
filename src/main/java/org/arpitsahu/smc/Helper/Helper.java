package org.arpitsahu.smc.Helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.security.Principal;

public class Helper {

    //This class is made to get back the email from the loged in user
    public static String getEmailOfLoggedInUser(Authentication authentication) {


        //agar humne login form use kiya hi to
        //OAuth2AuthenticationToken-> similar to that of OAuthConfig
        if(authentication instanceof OAuth2AuthenticationToken) {

            var oAuth2AuthenricationToken = (OAuth2AuthenticationToken) authentication;
            var clientId = oAuth2AuthenricationToken.getAuthorizedClientRegistrationId();

            //converting the principal to oAuth2User to get email
            var oauth2User=(OAuth2User)authentication.getPrincipal();
            String username="";

            //agar google use kiya hai to
            if(clientId.equalsIgnoreCase("google")){
                System.out.println("Getting email form google client");
                username=oauth2User.getAttribute("email").toString();
            }


            //agar github use kiya hai to
            else if (clientId.equalsIgnoreCase("github")) {
                System.out.println("Getting email form github client");
                username=oauth2User.getAttribute("email") !=null ? oauth2User.getAttribute("email").toString():oauth2User.getAttribute("login").toString()+"@gmail.com";


            }

            return username;
        }else{
            System.out.println("Getting data form local database");
            return authentication.getName();
        }
    }
}
