package org.arpitsahu.smc.Controller;

import org.arpitsahu.smc.Entities.Users;
import org.arpitsahu.smc.Helper.Helper;
import org.arpitsahu.smc.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.Principal;
import java.util.Objects;

//this class will handle all the user related requests
@Controller//this will return a html page
@RequestMapping("/SMC/user")
public class UserController {

    @Autowired
    private UserService userService;

    Logger log= LoggerFactory.getLogger(UserController.class);

    //instead of only giving user to profile request we will make it in such a way so that
    //it can be sent to all the request
    //we will make a class Root controller that will send a data to every request

    //user dashboard page
    @GetMapping("/dashboard")
    public String dashboard(){
        return "user/dashboard";
    }

    //user profile page
    @GetMapping("/profile")
    //instead of using principal we will use Authentication like of that we used in OAuth config
    public String profile(Model model, Authentication authentication){
        return "user/profile";
    }

    @GetMapping("/profile-pic")
    @ResponseBody
    public ResponseEntity<byte[]> profilePic(Authentication authentication) throws Exception {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        Users user = userService.getUserByEmail(username);

        if (user == null || user.getProfilePic() == null || user.getProfilePic().isBlank()) {
            return ResponseEntity.notFound().build();
        }

        URL imageUrl = new URL(user.getProfilePic());
        URLConnection connection = imageUrl.openConnection();
        String contentType = connection.getContentType();
        if (contentType == null || contentType.isBlank()) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        try (InputStream inputStream = connection.getInputStream()) {
            byte[] imageBytes = inputStream.readAllBytes();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(imageBytes);
        }
    }

    //user add contact page

    //usr edit contact page

    //user delete contact page
}
