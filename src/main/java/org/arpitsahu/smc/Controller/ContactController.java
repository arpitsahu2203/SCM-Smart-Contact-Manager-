package org.arpitsahu.smc.Controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.arpitsahu.smc.Entities.Contact;
import org.arpitsahu.smc.Entities.Users;
import org.arpitsahu.smc.Helper.AppConstants;
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
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import java.util.UUID;

@RequestMapping("/SMC/user/Contact")
@Controller
public class ContactController {

    @Autowired
    private imageService imageService;

    @Autowired
    private UserService userService;

    @Autowired
    private contactService contactService;

    @Autowired
    private View error;

    @GetMapping("/add")
    public String AddContact(Model model ){
        contactForm contactForm = new contactForm();
        model.addAttribute("contactForm",contactForm);
        return "user/AddContact";
    }

    @PostMapping("/add")
    public String ProcessingContact(@Valid @ModelAttribute contactForm contactForm, BindingResult bindingResult, Authentication authentication, HttpSession session){

        String image_id= UUID.randomUUID().toString();
        String username= Helper.getEmailOfLoggedInUser(authentication);
        Users user= userService.getUserByEmail(username);

        Logger logger= LoggerFactory.getLogger(this.getClass());
        if (contactForm.getContactImage() == null || contactForm.getContactImage().isEmpty()) {
            session.setAttribute("message", messageHelper.builder().type(messageEnum.red).content("Contact image is required").build());
            return "user/AddContact";
        }
        logger.info("Contact form received: {}", contactForm.getContactImage().getOriginalFilename());

        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(error->logger.info(error.toString()));
            session.setAttribute("message", messageHelper.builder().type(messageEnum.red).content("Details entered are invalid").build());
            return "user/AddContact";
        }

        String url=imageService.uploadImage(contactForm.getContactImage(),image_id);

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
        contact.setPicture(url);
        contact.setPublicImageId(image_id);
        contact.setUser(user);

        contactService.saveContact(contact);

        session.setAttribute("message", messageHelper.builder().type(messageEnum.green).content("Your contact have been saved").build());

        return "redirect:/SMC/user/Contact/add";
    }

    @GetMapping("/view")
    public String viewContacts(
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="size", defaultValue = "10") int size,
            @RequestParam(value="sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value="direstion", defaultValue = "ascending") String direction,
            Model model,Authentication authentication, HttpSession session){
        String username=Helper.getEmailOfLoggedInUser(authentication);

        Users user= userService.getUserByEmail(username);
        Page<Contact> pageContacts =contactService.getByUser(user, page, size, sortBy, direction);

        model.addAttribute("pageContacts",pageContacts);
        model.addAttribute("pageSize", AppConstants.Page_Size);
        return "user/contacts";
    }

    // Traditional delete handler
    @GetMapping("/delete/{id}")
    public String deleteContact(@PathVariable("id") String id,
                                Authentication authentication,
                                HttpSession session) {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        Users user = userService.getUserByEmail(username);
        Contact contact = contactService.getContactById(id);

        if (contact != null && contact.getUser() != null && contact.getUser().getId().equals(user.getId())) {
            contactService.deleteContactById(id);
            session.setAttribute("message", messageHelper.builder()
                    .type(messageEnum.green)
                    .content("Contact deleted successfully")
                    .build());
        } else {
            session.setAttribute("message", messageHelper.builder()
                    .type(messageEnum.red)
                    .content("Access denied to delete contact")
                    .build());
        }

        return "redirect:/SMC/user/Contact/view";
    }

    // View contact update page
    @GetMapping("/view/{id}")
    public String updateContactFormView(@PathVariable("id") String id,
                                        Model model,
                                        Authentication authentication) {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        Users user = userService.getUserByEmail(username);
        Contact contact = contactService.getContactById(id);

        if (contact == null || contact.getUser() == null || !contact.getUser().getId().equals(user.getId())) {
            return "redirect:/SMC/user/Contact/view";
        }

        contactForm form = new contactForm();
        form.setName(contact.getName());
        form.setEmail(contact.getEmail());
        form.setPhoneNumber(contact.getPhoneNumber());
        form.setAddress(contact.getAddress());
        form.setDescription(contact.getDescription());
        form.setFavourite(contact.isFavorite());
        form.setWebsiteLink(contact.getWebsiteLink());
        form.setLinkedinLink(contact.getLinkedinLink());
        form.setTwitterLink(contact.getTwitterLink());
        form.setInstagramLink(contact.getInstagramLink());
        form.setPicture(contact.getPicture());

        model.addAttribute("contactForm", form);
        model.addAttribute("contactId", id);

        return "user/UpdateContact";
    }

    // Process contact update
    @PostMapping("/update/{id}")
    public String updateContactProcessing(@PathVariable("id") String id,
                                          @Valid @ModelAttribute contactForm contactForm,
                                          BindingResult bindingResult,
                                          Model model,
                                          Authentication authentication,
                                          HttpSession session) {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        Users user = userService.getUserByEmail(username);
        Contact contact = contactService.getContactById(id);

        if (contact == null || contact.getUser() == null || !contact.getUser().getId().equals(user.getId())) {
            session.setAttribute("message", messageHelper.builder().type(messageEnum.red).content("Access denied").build());
            return "redirect:/SMC/user/Contact/view";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("contactForm", contactForm);
            model.addAttribute("contactId", id);
            return "user/UpdateContact";
        }

        // Update fields
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setFavorite(contactForm.isFavourite());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setLinkedinLink(contactForm.getLinkedinLink());
        contact.setTwitterLink(contactForm.getTwitterLink());
        contact.setInstagramLink(contactForm.getInstagramLink());

        // Process image if user uploaded a new one
        if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
            String imageId = UUID.randomUUID().toString();
            String url = imageService.uploadImage(contactForm.getContactImage(), imageId);
            contact.setPicture(url);
            contact.setPublicImageId(imageId);
        }

        contactService.updateContact(contact);
        session.setAttribute("message", messageHelper.builder().type(messageEnum.green).content("Contact updated successfully").build());

        return "redirect:/SMC/user/Contact/view";
    }
}
