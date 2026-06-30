package org.arpitsahu.smc.Services;

import java.util.List;
import java.util.Optional;

import org.arpitsahu.smc.Entities.Users;

// This is a Service Interface
// It defines the CONTRACT (what operations are available) for user-related business logic
// The actual implementation is in UserServiceImpl (marked with @Service)
// Controller talks to this interface, NOT directly to the implementation → loose coupling
public interface UserService {

    // Saves a new user to the database and returns the saved user object
    Users saveUser(Users user);

    // Fetches a user by their ID → returns Optional to handle "user not found" gracefully
    Optional<Users> getUsersById(String id);

    // Updates an existing user's data → returns Optional of the updated user
    Optional<Users> updateUsers(Users user);

    // Deletes a user by their ID
    void deleteUsers(String id);

    // Returns true if a user with the given ID exists, false otherwise
    boolean isUsersExist(String id);

    // Returns true if a user with the given Email exists → useful for checking duplicates during registration
    boolean isUsersExistByEmail(String email);

    // Returns a list of ALL users in the database
    List<Users> getAllUsers();

    Users getUserByEmail(String email);
}