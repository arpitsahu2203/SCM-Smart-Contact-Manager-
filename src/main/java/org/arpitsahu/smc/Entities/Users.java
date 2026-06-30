package org.arpitsahu.smc.Entities;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
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
public class Users implements UserDetails {
    
    @Id
    public String id;
    @Column(unique=true,nullable=false)
    public String email;
    @Column(nullable=false,length=80)
    @Getter(value = AccessLevel.NONE) // we will handle email verification manually, so hide the getter
    public String password;
    @Column(nullable=false,length=50)
    public String name;
    @Column(length=10000)
    public String profilePic;
    @Column(length=500)
    public String about;
    @Column(unique=true,length=10)
    public String phoneNumber;

    //Information
    @Builder.Default
    private boolean enabled=false;
    @Builder.Default
    private boolean emailVerified=false;
    @Builder.Default
    private boolean phoneNumberVerified =false;

    //Social Logins like Google, facebook, etc.
    @Builder.Default
    @Enumerated(value=EnumType.STRING)
    private Providers provider=Providers.SELF;
    private String providerId;

    @OneToMany(mappedBy = "user",cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.LAZY)
    @Builder.Default
    private List<Contact> contacts=new ArrayList<>();


    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList=new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //list of roles[USER,ADMIN]
        //Collection of SimpleGrantedAuthority [ROLE_USER,ROLE_ADMIN]
        Collection<SimpleGrantedAuthority> roles= roleList.stream().map(role-> new SimpleGrantedAuthority(role)).toList();

        return roles;
    }

    @Override
    public @Nullable String getPassword() {
       return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
