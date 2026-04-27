package org.arpitsahu.smc.Repository;

import java.util.Optional;

import org.arpitsahu.smc.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users, String>{
    //all the custom methods are written here

    //here we created a method to find user by email, spring data jpa will automatically implement this method based on the method name
    Optional<Users> findByEmail(String email);
}
