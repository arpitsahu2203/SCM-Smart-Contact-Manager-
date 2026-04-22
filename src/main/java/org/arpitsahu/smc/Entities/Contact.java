package org.arpitsahu.smc.Entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;


@Entity
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

    @ManyToOne
    private Users user;

    @OneToMany(mappedBy= "contact",cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.LAZY)
    private List<SocialLink> socialLinks=new ArrayList<>();
}
