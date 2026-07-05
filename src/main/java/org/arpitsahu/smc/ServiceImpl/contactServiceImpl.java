package org.arpitsahu.smc.ServiceImpl;

import org.arpitsahu.smc.Entities.Contact;
import org.arpitsahu.smc.Helper.ResourceNotFoundException;
import org.arpitsahu.smc.Repository.contactRepo;
import org.arpitsahu.smc.Services.contactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class contactServiceImpl implements contactService {

    @Autowired
    private contactRepo contactRepo;

    @Override
    public Contact saveContact(Contact contact) {
        String contactid= UUID.randomUUID().toString();
        contact.setId(contactid);

        contactRepo.save(contact);
        return contact;
    }

    @Override
    public Contact updateContact(Contact contact) {
        return null;
    }


    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getContactById(String id) {
        return contactRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Contact not found with given id"+id));
    }

    @Override
    public void deleteContactById(String id) {

        Contact contact=contactRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Contact not found with given id"+id));
        contactRepo.delete(contact);

    }

    @Override
    public List<Contact> search(String name, String email, String phoneNumber) {
        return List.of();
    }

    @Override
    public List<Contact> getByUserId(String id) {

        return contactRepo.findByUserId(id);
    }


}
