package org.arpitsahu.smc.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.arpitsahu.smc.Entities.Providers;
import org.arpitsahu.smc.Entities.Users;
import org.arpitsahu.smc.Helper.AppConstants;
import org.arpitsahu.smc.Repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    //A logger in Spring Boot is a tool used to capture, format, and record runtime events, errors, and informational messages produced by an application
    Logger logger= LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //here we are going to collect data form the user that is being authenticated directly from the authenticate
        //this brings info about the token
        var oauth2Authentication=(OAuth2AuthenticationToken) authentication;

        //here the token provider comes out as a string
        String authorizedClientRegistrationId = oauth2Authentication.getAuthorizedClientRegistrationId();

        logger.info(authorizedClientRegistrationId);

        //we will collect data based on the type of Provider is being used by  the user

        var oauthUser= (DefaultOAuth2User) authentication.getPrincipal();

        //this brings all the attributes in the Oauth in the form of key value pair and print it on the console
        oauthUser.getAttributes().forEach((key,value)->{
            logger.info(key+":"+value);
        });


        //here we set data in the form of user1
        Users user1=new Users();
        user1.setId(UUID.randomUUID().toString());
        user1.setRoleList(List.of(AppConstants.ROLE_USER));
        user1.setEmailVerified(true);
        user1.setEnabled(true);
        user1.setPassword("Password");


        if(authorizedClientRegistrationId.equalsIgnoreCase("google")){
            //Google
            //based on the info the auth provider provides we have to define the data of how to be coolected so the correct data is stored

            user1.setName(oauthUser.getAttribute("name").toString());
            user1.setProviderId(oauthUser.getName().toString());
            user1.setProfilePic(oauthUser.getAttribute("picture").toString());
            user1.setEmail(oauthUser.getAttribute("email").toString());
            user1.setProvider(Providers.GOOGLE);

        }else if( authorizedClientRegistrationId.equalsIgnoreCase("github")){
            //Github

            String email=oauthUser.getAttribute("email") !=null ? oauthUser.getAttribute("email").toString():oauthUser.getAttribute("login").toString()+"@gmail.com";
            String picture= oauthUser.getAttribute("avatar_url").toString();
            String name=oauthUser.getAttribute("login") !=null ? oauthUser.getAttribute("login").toString():oauthUser.getAttribute("name").toString();
            String providerUserId=oauthUser.getName();

            user1.setEmail(email);
            user1.setProfilePic(picture);
            user1.setName(name);
            user1.setProviderId(providerUserId);
            user1.setProvider(Providers.GITHUB);

        }






        /*
         DefaultOAuth2User user=(DefaultOAuth2User)authentication.getPrincipal();

         logger.info(user.getName());

         user.getAttributes().forEach((key,value)->{
             logger.info("{}:{}", key, value);
         });

         logger.info(user.getAuthorities().toString());


        //here we are configuring on how to save user data from google or Oauth to our database before getting redirected
        String email=user.getAttribute("email").toString();
        String name=user.getAttribute("name").toString();
        String picture=user.getAttribute("picture").toString();

        //converting the collected data to a user
        Users user1=new Users();
        user1.setEmail(email);
        user1.setName(name);
        user1.setProfilePic(picture);
        user1.setId(UUID.randomUUID().toString());
        user1.setEnabled(true);
        user1.setEmailVerified(true);
        user1.setProviderId(user.getName());
        user1.setRoleList(List.of(AppConstants.ROLE_USER));
        user1.setAbout("This id is created using google..");
        user1.setProvider(Providers.GOOGLE);

        user1.setPassword("password");//As password cannot be set null
         */



        //Now we will save the user to database by using UserRepository

        Users user2=userRepo.findByEmail(user1.getEmail()).orElse(null);//if user is found the user2 is set null

        if(user2==null){//if user2 is not null ie user doesn't exist thus save user to the database;
            userRepo.save(user1);
            logger.info("User saved: "+ user1.getEmail());
        } else {
            // Update existing user with latest profile picture and name
            user2.setProfilePic(user1.getProfilePic());
            user2.setName(user1.getName());
            userRepo.save(user2);
            logger.info("User updated: "+ user2.getEmail());
        }



        //here we implemented the AuthenticationSuccessHandler so that as soon as the login success then the request is redirected to dashboard
        logger.info("OAuthAuthenticationSuccessHandler");

        response.sendRedirect("/SMC/user/profile");
    }
}
