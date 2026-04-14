package org.arpitsahu.smc.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller//will return a html page upon calls
@RequestMapping("/SMC")
public class MyController {

    @GetMapping("/home")
    public String Home(Model mod){
        mod.addAttribute("name","Arpit Sahu");
        mod.addAttribute("work","Java Developer");
        return "home";
    }
}
