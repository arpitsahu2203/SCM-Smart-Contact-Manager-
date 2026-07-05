package org.arpitsahu.smc.Controller;

import org.arpitsahu.smc.Entities.Users;
import org.arpitsahu.smc.Helper.messageEnum;
import org.arpitsahu.smc.Helper.messageHelper;
import org.arpitsahu.smc.Services.UserService;
import org.arpitsahu.smc.forms.UserForms;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller//will return a html page upon calls
@RequestMapping("/SMC")
public class PageController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String index(){
        return "redirect:/SMC/home";
    }

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

    

    //THIS IS REGISTRATION CONTROLLER
    //form here we are sending request for the form
    @GetMapping("/register")
    public String register(Model model){
        UserForms userform=new UserForms();
        //we are sending an empty form to the HTML page so that we can bind the data to it when the form is submitted
        //so it replaces the form with an empty object form and all the data is stored in that object that is later used to save the data in the database
        model.addAttribute("userform",userform);
        return "register";
    }


    //processing registration
    @PostMapping("/do-register")//It tells Spring: **"When a POST request comes to `/do-register`, run this method."**
    //Binding result hold the result of@Valid that validates the form
    //Model Attribute: it takes the incoming data ie form aor any view and converts it to an object that can be further used
    public String processRegister(@Valid @ModelAttribute("userform") UserForms userform,BindingResult rBindingResult, HttpSession session){
        System.out.print("Registration successful!");
        //fetch data-->we will create a new class for receiveing data from the form
        System.out.print(userform);


        //validate form data-->we will do it later
        //if there are any error then the data will not be processed further and the register page will be returned
        if(rBindingResult.hasErrors()){
            return "register";
        }

        //save data to database
        //userService
        //UserForm-->User-->save to database
        Users user=Users.builder().
        name(userform.getName()).
        email(userform.getEmail()).
        phoneNumber(userform.getPhoneNumber()).
        about(userform.getAbout()).
        password(userform.getPassword()).
        build();

        Users savedUser=userService.saveUser(user);
        System.out.println("User saved");

        //add the message
        //here we are going to use sessions
        messageHelper message=messageHelper.builder().content("Registration successful").type(messageEnum.green).build();

        session.setAttribute("message", message);

        
        return "redirect:/SMC/register";//redirecting to register page after registration
    }
}
