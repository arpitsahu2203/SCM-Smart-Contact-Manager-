package org.arpitsahu.smc.Controller;

import jakarta.servlet.http.HttpSession;
import org.arpitsahu.smc.Entities.Users;
import org.arpitsahu.smc.Helper.Helper;
import org.arpitsahu.smc.Helper.messageEnum;
import org.arpitsahu.smc.Helper.messageHelper;
import org.arpitsahu.smc.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

//this class will handle all the user related requests
@Controller
@RequestMapping("/SMC/user")
public class UserController {

    @Autowired
    private UserService userService;

    Logger log = LoggerFactory.getLogger(UserController.class);

    //user dashboard page
    @GetMapping("/dashboard")
    public String dashboard(){
        return "user/dashboard";
    }

    //user profile page
    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication){
        return "user/profile";
    }

    //user profile update
    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam("name") String name,
                                @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                                @RequestParam(value = "about", required = false) String about,
                                Authentication authentication,
                                HttpSession session) {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        Users user = userService.getUserByEmail(username);

        if (user != null) {
            user.setName(name);
            user.setPhoneNumber(phoneNumber);
            user.setAbout(about);
            userService.updateUsers(user);
            session.setAttribute("message", messageHelper.builder()
                    .type(messageEnum.green)
                    .content("Profile updated successfully")
                    .build());
        }
        return "redirect:/SMC/user/profile";
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
}
