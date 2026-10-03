package org.arpitsahu.smc.Controller;

import org.arpitsahu.smc.Entities.Contact;
import org.arpitsahu.smc.Entities.Users;
import org.arpitsahu.smc.Helper.Helper;
import org.arpitsahu.smc.Helper.ResourceNotFoundException;
import org.arpitsahu.smc.Services.UserService;
import org.arpitsahu.smc.Services.contactService;
import org.arpitsahu.smc.payload.ContactResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/SMC/api/contacts")
public class ContactApiController {

    private static final Logger logger = LoggerFactory.getLogger(ContactApiController.class);

    @Autowired
    private contactService contactService;

    @Autowired
    private UserService userService;

    /**
     * AJAX endpoint to fetch full contact information dynamically without page reload.
     * Enforces user ownership of the contact record.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getContactById(@PathVariable("id") String id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        try {
            String loggedInEmail = Helper.getEmailOfLoggedInUser(authentication);
            Users user = userService.getUserByEmail(loggedInEmail);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }

            Contact contact = contactService.getContactById(id);

            // Security verification: ensure contact belongs to logged-in user
            if (contact.getUser() == null || !contact.getUser().getId().equals(user.getId())) {
                logger.warn("Unauthorized attempt to access contact {} by user {}", id, loggedInEmail);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied for this contact");
            }

            ContactResponseDto responseDto = ContactResponseDto.fromEntity(contact);
            return ResponseEntity.ok(responseDto);

        } catch (ResourceNotFoundException e) {
            logger.error("Contact not found with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(java.util.Map.of("message", "Contact not found"));
        } catch (Exception e) {
            logger.error("Error retrieving contact with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(java.util.Map.of("message", "Failed to load contact details"));
        }
    }

    /**
     * AJAX endpoint to delete a contact.
     * Enforces user ownership of the contact record.
     */
    @org.springframework.web.bind.annotation.DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContactById(@PathVariable("id") String id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(java.util.Map.of("message", "User not authenticated"));
        }

        try {
            String loggedInEmail = Helper.getEmailOfLoggedInUser(authentication);
            Users user = userService.getUserByEmail(loggedInEmail);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(java.util.Map.of("message", "User not found"));
            }

            Contact contact = contactService.getContactById(id);

            // Ownership check
            if (contact.getUser() == null || !contact.getUser().getId().equals(user.getId())) {
                logger.warn("Unauthorized attempt to delete contact {} by user {}", id, loggedInEmail);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(java.util.Map.of("message", "Access denied for this contact"));
            }

            contactService.deleteContactById(id);
            logger.info("Contact {} deleted successfully by user {}", id, loggedInEmail);

            return ResponseEntity.ok(java.util.Map.of(
                    "success", true,
                    "message", "Contact deleted successfully",
                    "contactId", id
            ));

        } catch (ResourceNotFoundException e) {
            logger.error("Contact not found for deletion with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(java.util.Map.of("message", "Contact not found"));
        } catch (Exception e) {
            logger.error("Error deleting contact with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(java.util.Map.of("message", "Failed to delete contact"));
        }
    }

    /**
     * AJAX endpoint to update a contact profile without full page reload.
     */
    @org.springframework.web.bind.annotation.PutMapping("/{id}")
    public ResponseEntity<?> updateContactById(@PathVariable("id") String id,
                                               @org.springframework.web.bind.annotation.RequestBody ContactResponseDto updateDto,
                                               Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(java.util.Map.of("message", "User not authenticated"));
        }

        try {
            String loggedInEmail = Helper.getEmailOfLoggedInUser(authentication);
            Users user = userService.getUserByEmail(loggedInEmail);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(java.util.Map.of("message", "User not found"));
            }

            Contact contact = contactService.getContactById(id);

            // Ownership check
            if (contact.getUser() == null || !contact.getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(java.util.Map.of("message", "Access denied for this contact"));
            }

            if (updateDto.getName() != null && !updateDto.getName().isBlank()) {
                contact.setName(updateDto.getName().trim());
            }
            if (updateDto.getEmail() != null) {
                contact.setEmail(updateDto.getEmail().trim());
            }
            if (updateDto.getPhoneNumber() != null) {
                contact.setPhoneNumber(updateDto.getPhoneNumber().trim());
            }
            if (updateDto.getAddress() != null) {
                contact.setAddress(updateDto.getAddress().trim());
            }
            if (updateDto.getDescription() != null) {
                contact.setDescription(updateDto.getDescription().trim());
            }
            contact.setFavorite(updateDto.isFavorite());
            contact.setLinkedinLink(updateDto.getLinkedinLink());
            contact.setTwitterLink(updateDto.getTwitterLink());
            contact.setWebsiteLink(updateDto.getWebsiteLink());
            contact.setInstagramLink(updateDto.getInstagramLink());

            Contact updated = contactService.updateContact(contact);
            return ResponseEntity.ok(ContactResponseDto.fromEntity(updated));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(java.util.Map.of("message", "Contact not found"));
        } catch (Exception e) {
            logger.error("Error updating contact with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(java.util.Map.of("message", "Failed to update contact"));
        }
    }
}
