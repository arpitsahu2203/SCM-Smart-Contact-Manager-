package org.arpitsahu.smc.ServiceImpl;

import org.arpitsahu.smc.Entities.Users;
import org.arpitsahu.smc.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SecurityCustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //Yahan par humlog apne user ko load karenge

       Users user=userRepo.findByEmail(username)
       .orElseThrow(()->new UsernameNotFoundException("User not found with email: "+username));

        log.info("User found: {}", user.getUsername());
        log.info("User enabled: {}", user.isEnabled());
        log.info("Password hash: {}", user.getPassword());

       return user; // since Users implements UserDetails, we can directly return the user entity
    }

}
