package org.arpitsahu.smc.Controller;

import org.arpitsahu.smc.Entities.Users;
import org.arpitsahu.smc.Helper.Helper;
import org.arpitsahu.smc.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

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

    //user add contact page

    //usr edit contact page

    //user delete contact page
}
