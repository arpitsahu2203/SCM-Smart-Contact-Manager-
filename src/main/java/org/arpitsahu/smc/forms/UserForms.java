package org.arpitsahu.smc.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message="Name is required")
    @Size(min=3, max=30, message="Name must be between 3-30 characters")
    public String name;

    @NotBlank(message="Password is required")
    @Size(min=6, max=15, message="Password must contain 6-15 characters")
    public String password;        // ✅ lowercase p

    @NotBlank(message="Phone number is Required")
    @Size(min=10, max=10, message="Phone number must contain 10 characters")
    public String phoneNumber;     // ✅ camelCase

    @NotBlank(message="Email is required")
    @Email(message="Invalid Email")
    public String email;           // ✅ lowercase e

    @NotBlank(message="About is required")
    public String about;
}