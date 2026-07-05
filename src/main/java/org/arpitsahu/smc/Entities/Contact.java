package org.arpitsahu.smc.Entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contact {
    
    @Id
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String picture;
    private boolean favorite=false;
    private String instagramLink;
    private String twitterLink;
    private String linkedinLink;
    private String websiteLink;
    @Column(length=1000)
    private String description;
    private String publicImageId;

    @ManyToOne
    private Users user;

    @OneToMany(mappedBy= "contact",cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.LAZY)
    @Builder.Default
    private List<SocialLink> socialLinks=new ArrayList<>();
}
