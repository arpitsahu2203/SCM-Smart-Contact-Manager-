package org.arpitsahu.smc.Controller;


import com.nimbusds.oauth2.sdk.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.arpitsahu.smc.Entities.Contact;
import org.arpitsahu.smc.Entities.Users;
import org.arpitsahu.smc.Helper.Helper;
import org.arpitsahu.smc.Helper.messageEnum;
import org.arpitsahu.smc.Helper.messageHelper;
import org.arpitsahu.smc.Services.UserService;
import org.arpitsahu.smc.Services.contactService;
import org.arpitsahu.smc.Services.imageService;
import org.arpitsahu.smc.forms.contactForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;

import java.util.List;
import java.util.UUID;

@RequestMapping("/SMC/user/Contact")
@Controller
public class ContactController {

    @Autowired
    private imageService imageService;

    @Autowired
    private UserService userService;

    //wew will autowire contact service so that it can be used in processing contact
    @Autowired
    private contactService contactService;

    @Autowired
    private View error;

    @GetMapping("/add")
    //Model: It sends an empty contactForm to the webpage where the information is filled
    public String AddContact(Model model ){
        contactForm contactForm = new contactForm();

        model.addAttribute("contactForm",contactForm);
        return "user/AddContact";
    }

    @PostMapping("/add")
    //ModelAttribute: It collects the filled form and canvert it to java object
    //BindingResult: it holds the result of @Valid
    public String ProcessingContact(@Valid @ModelAttribute contactForm contactForm, BindingResult bindingResult, Authentication authentication, HttpSession session){

        String image_id= UUID.randomUUID().toString();

        //getting user form authentication
        String username= Helper.getEmailOfLoggedInUser(authentication);

        Users user= userService.getUserByEmail(username);

        Logger logger= LoggerFactory.getLogger(this.getClass());
        logger.info("Contact form received: {}", contactForm.getContactImage().getOriginalFilename());

        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(error->logger.info(error.toString()));
            session.setAttribute("message", messageHelper.builder().type(messageEnum.red).content("Details entered are invalid").build());
            return "user/AddContact";
        }

        //here we will get the form and we will process it according to our needs

        //Saving information to database

        //here we are gpoing to image service that will process our image and store it to cloudnary and then return a url
        String url=imageService.uploadImage(contactForm.getContactImage(),image_id);

        //to do this we have to call user service to store data as it holds the Contact repo and save contact method
        //-> So to save contact we have to pass contact object to the saveContact method of contactService
        //->but we dont have it so we will create a new contact object and set the values from the form to it and then pass it to the saveContact method of contactService
        Contact contact=new Contact();

        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setFavorite(contactForm.isFavourite());
        contact.setInstagramLink(contactForm.getInstagramLink());
        contact.setLinkedinLink(contactForm.getLinkedinLink());
        contact.setTwitterLink(contactForm.getTwitterLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        //here we are uploading the image url from cloudinary to our database
        contact.setPicture(url);
        //this is the id we created for our image that is stored in cloudinary so if we want to change link implementation we can be able to do that
        contact.setPublicImageId(image_id);
        contact.setUser(user);

        //Here the image will get processed and the link will be stored in the database
        //Here we will get the image link and save it

        //then we pass the user to the user service
        contactService.saveContact(contact);

        session.setAttribute("message", messageHelper.builder().type(messageEnum.green).content("Your contact have been saved").build());

        return "redirect:/SMC/user/Contact/add";
    }


    @GetMapping("/view")
    public String viewContacts(Model model,Authentication authentication, HttpSession session){
        String username=Helper.getEmailOfLoggedInUser(authentication);

        Users user= userService.getUserByEmail(username);
        List<Contact> contacts =contactService.getByUser(user);

        model.addAttribute("contacts",contacts);
        return "user/contacts";
    }


}
