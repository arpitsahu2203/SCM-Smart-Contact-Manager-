package org.arpitsahu.smc.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.arpitsahu.smc.Entities.Contact;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactResponseDto {

    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String picture;
    private boolean favorite;
    private String instagramLink;
    private String twitterLink;
    private String linkedinLink;
    private String websiteLink;
    private String description;

    public static ContactResponseDto fromEntity(Contact contact) {
        if (contact == null) return null;

        return ContactResponseDto.builder()
                .id(contact.getId())
                .name(contact.getName())
                .email(contact.getEmail())
                .phoneNumber(contact.getPhoneNumber())
                .address(contact.getAddress())
                .picture(contact.getPicture())
                .favorite(contact.isFavorite())
                .instagramLink(contact.getInstagramLink())
                .twitterLink(contact.getTwitterLink())
                .linkedinLink(contact.getLinkedinLink())
                .websiteLink(contact.getWebsiteLink())
                .description(contact.getDescription())
                .build();
    }
}
