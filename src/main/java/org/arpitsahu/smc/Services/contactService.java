package org.arpitsahu.smc.Services;

import org.arpitsahu.smc.Entities.Contact;
import org.arpitsahu.smc.Entities.Users;
import org.springframework.data.domain.Page;

import java.util.List;

public interface contactService {

    Contact saveContact(Contact contact);

    Contact updateContact(Contact contact);

    List<Contact> getAll();

    Contact getContactById(String id);

    void deleteContactById(String id);

    List<Contact> search(String name, String email, String phoneNumber);

    List<Contact> getByUserId(String id);

    Page<Contact> getByUser(Users user,int page, int size, String sortBy, String direction);
}
