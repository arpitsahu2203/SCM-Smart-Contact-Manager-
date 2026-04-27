package org.arpitsahu.smc.Controller;

import org.arpitsahu.smc.Entities.Users;
import org.arpitsahu.smc.Services.UserService;
import org.arpitsahu.smc.forms.UserForms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller//will return a html page upon calls
@RequestMapping("/SMC")
public class MyController {

    @Autowired
    UserService userService;

    @GetMapping("/home")
    public String Home(Model mod){
        mod.addAttribute("name","Arpit Sahu");
        mod.addAttribute("work","Java Developer");
        return "home";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }

    @GetMapping("/services")
    public String services(){
        return "services";
    }

    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }


    //form here we are sending request for the form
    @GetMapping("/register")
    public String register(Model model){
        UserForms userform=new UserForms();
        //we are sending an empty form to the html page so that we can bind the data to it when the form is submitted
        //so it replaces the form with an empty object form and all the data is stored in that object that is later used to save the data in the database
        model.addAttribute("userform",userform);
        return "register";
    }

    //provesing register

    @PostMapping("/do-register")//It tells Spring: **"When a POST request comes to `/do-register`, run this method."**
    public String processRegister(@ModelAttribute UserForms userform){
        System.out.print("Registration successful!");
        //fetch data-->we will create a new class for receiveing data from the form
        System.out.print(userform);
        //validate form data-->we will do it later


        //save data to database
        //userService
        //UserForm-->User-->save to database
        Users user=Users.builder().
        name(userform.getName()).
        Email(userform.getEmail()).
        PhoneNumber(userform.getPhoneNumber()).
        about(userform.getAbout()).
        Password(userform.getPassword()).
        build();

        Users savedUser=userService.saveUser(user);
        System.out.println("User saved");
        
        return "redirect:/SMC/register";//redirecting to register page after registration
    }
}
