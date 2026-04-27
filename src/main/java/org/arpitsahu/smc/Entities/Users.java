package org.arpitsahu.smc.Entities;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="User")
@Table(name="Users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Users {
    
    @Id
    public String Id;
    @Column(unique=true,nullable=false)
    public String Email;
    @Column(nullable=false,length=50)
    public String Password;
    @Column(nullable=false,length=50)
    public String name;
    @Column(length=10000)
    public String profilePic;
    @Column(length=500)
    public String about;
    @Column(unique=true,length=10)
    public String PhoneNumber;

    //Information
    @Builder.Default
    private boolean enabled=false;
    @Builder.Default
    private boolean emailVerified=false;
    @Builder.Default
    private boolean phoneNumberVerfied=false;

    //Social Logins like google, facebook, etc.
    @Builder.Default
    @Enumerated
    private Providers provider=Providers.SELF;
    private String providerId;

    @OneToMany(mappedBy = "user",cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.LAZY)
    @Builder.Default
    private List<Contact> contacts=new ArrayList<>();
}
