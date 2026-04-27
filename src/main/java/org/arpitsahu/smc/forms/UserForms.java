package org.arpitsahu.smc.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// This is a Form class (DTO - Data Transfer Object)
// It is used to RECEIVE data from the HTML registration form
// It is NOT an entity - it does not map to any database table
// Spring binds the form fields to this object automatically via @ModelAttribute in the controller

@Getter          // Lombok: generates getters for all fields
@Setter          // Lombok: generates setters for all fields
@NoArgsConstructor  // Lombok: generates empty constructor (required by Spring for binding)
@AllArgsConstructor // Lombok: generates constructor with all fields
@Builder         // Lombok: enables builder pattern → UserForms.builder().name("Arpit").build()
@ToString        // Lombok: generates toString() → useful for System.out.print(userform) in controller
public class UserForms {
    public String name;       // maps to name field in the HTML form
    public String Password;   // maps to password field in the HTML form
    public String PhoneNumber;// maps to phone number field in the HTML form
    public String Email;      // maps to email field in the HTML form
    public String about;      // maps to about field in the HTML form
}