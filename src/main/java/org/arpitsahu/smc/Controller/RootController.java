package org.arpitsahu.smc.Controller;

import org.arpitsahu.smc.Entities.Users;
import org.arpitsahu.smc.Helper.Helper;
import org.arpitsahu.smc.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

//THIS ANNOTATION SEND THE  DATA IN THE CLASS WITH EVERY REQUEST BEING EXECUTED THROUGHOUT THE PROGRAM
@ControllerAdvice
public class RootController {

    @Autowired
    private UserService userService;

    Logger log= LoggerFactory.getLogger(RootController.class);

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication){
        if(authentication==null){
            return;
        }
        System.out.println("Adding logged in user data to the model");

        //here we get the info about the logged-in user from spring security
        //this is sending principal to the helper class in helper tha defines the logic of how the data should be picked
        String username= Helper.getEmailOfLoggedInUser(authentication);

        //this prints the user login id
        //but this will print different stuff for each type of login like for oauth it returns a id number but for form
        //it will return an email id
        log.info("User logged in : {}", username);

        //database se data ko fetch : get user from db
        //we will use UsersService in the database
        Users users=userService.getUserByEmail(username);

        if(users==null){
            model.addAttribute("message","User not found");
        }else{
            //we will use odel to get the info from the users to the web page
            model.addAttribute("LoggedInUser",users);
        }
    }
}
