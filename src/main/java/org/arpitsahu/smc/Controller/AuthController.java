package org.arpitsahu.smc.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    //THIS IS LOGIN CONTROLLER SHOWING LOGIN PAGE
    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
