package org.arpitsahu.smc.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocialLink {
    @Id
    private String id;
    private String platform;
    @Column(length=1000)
    private String url;
    
    @ManyToOne
    private Contact contact;
}

