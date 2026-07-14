package org.arpitsahu.smc.Services;

import org.arpitsahu.smc.Entities.Contact;
import org.arpitsahu.smc.Entities.Users;

import java.util.List;
import java.util.Optional;

public interface contactService {

    Contact saveContact(Contact contact);

    Contact updateContact(Contact contact);

    List<Contact> getAll();

    Contact getContactById(String id);

    void deleteContactById(String id);

    List<Contact> search(String name, String email, String phoneNumber);

    List<Contact> getByUserId(String id);

    List<Contact> getByUser(Users user);


}
