package org.arpitsahu.smc.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//this class will handle all the user related requests
@Controller//this will return a html page
@RequestMapping("/SMC/user")
public class UserController {

    //user dashboard page
    @GetMapping("/dashboard")
    public String dashboard(){
        return "user/dashboard";
    }

    //user profile page
    @GetMapping("/profile")
    public String profile(){
        return "user/profile";
    }

    //user add contact page

    //usr edit contact page

    //user delete contact page
}
