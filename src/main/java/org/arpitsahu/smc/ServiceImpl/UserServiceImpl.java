package org.arpitsahu.smc.ServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.arpitsahu.smc.Entities.Users;
import org.arpitsahu.smc.Helper.AppConstants;
import org.arpitsahu.smc.Helper.ResourceNotFoundException;
import org.arpitsahu.smc.Repository.UserRepo;
import org.arpitsahu.smc.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;


// This is the Service Implementation class
// It implements the UserService interface and contains the actual business logic
// @Service → marks this as a Spring-managed bean so @Autowired works in the controller
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    // Spring automatically injects the UserRepo bean here (no need to manually create it)
    @Autowired
    private UserRepo userrepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public Users saveUser(Users user) {
        // Since our ID is a String (not auto-increment), we manually generate a unique ID
        // UUID.randomUUID() generates a globally unique string like "a3f9b2c1-..."
        String userId = UUID.randomUUID().toString();
        user.setId(userId);

        //password encoder
        //encoding password through BCrypt
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //setting roles
        user.setRoleList(List.of(AppConstants.ROLE_USER));


        user.setEnabled(true);


        return userrepo.save(user); // saves to DB and returns the saved entity
    }

    @Override
    public Optional<Users> getUsersById(String id) {
        // findById returns Optional<Users> → empty if not found, present if found
        return userrepo.findById(id);
    }

    @Override
    public Optional<Users> updateUsers(Users user) {
        // First fetch the existing user from DB → throw exception if not found
        Users user2 = userrepo.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Copy all new values from incoming user → into the existing DB user (user2)
        user2.setEmail(user.getEmail());
        user2.setAbout(user.getAbout());
        user2.setName(user.getName());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneNumberVerified(user.isPhoneNumberVerified());
        user2.setProvider(user.getProvider());
        user2.setProviderId(user.getProviderId()); // fixed: was incorrectly using user2
        user2.setEnabled(user.isEnabled());

        // Save updated user2 back to DB
        Users save = userrepo.save(user2);
        return Optional.of(save); // wrap in Optional and return
    }

    @Override
    public void deleteUsers(String id) {
        // Fetch user first → throw exception if not found (avoid silent failure)
        Users user2 = userrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userrepo.delete(user2); // delete the fetched entity from DB
    }

    @Override
    public boolean isUsersExist(String id) {
        // orElse(null) → returns null if user not found (no exception thrown)
        Users user2 = userrepo.findById(id).orElse(null);
        return user2 != null; // simplified boolean return

    }

    @Override
    public boolean isUsersExistByEmail(String email) {
        // findByEmail is a custom query method defined in UserRepo
        // orElse(null) → returns null if not found (no exception)
        Users user2 = userrepo.findByEmail(email).orElse(null);
        return user2 != null; // simplified boolean return
    }

    @Override
    public List<Users> getAllUsers() {
        // findAll() → returns all users from the DB as a List
        return userrepo.findAll();
    }

    @Override
    public Users getUserByEmail(String email) {
        return userrepo.findByEmail(email).orElse(null);
    }
}