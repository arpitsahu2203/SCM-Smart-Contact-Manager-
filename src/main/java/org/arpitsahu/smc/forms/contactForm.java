package org.arpitsahu.smc.forms;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class contactForm {
    @NotBlank(message = "Name is required")
    public String name;

    @NotBlank(message="Email is required")
    @Email(message = "Invalid Email")
    public String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must contain 10 digits")
    public String phoneNumber;

    @NotBlank(message = "Address is required")
    public String address;

    public String picture;

    public boolean favourite;

    public String instagramLink;

    public String twitterLink;

    public String linkedinLink;

    public String websiteLink;

    public String description;

    //yahan par hum ek annotation banayege jo humari file ko validate karega
    //Wo file ka size aur resolution check karega
    public MultipartFile contactImage; //this is used for contact image
}
