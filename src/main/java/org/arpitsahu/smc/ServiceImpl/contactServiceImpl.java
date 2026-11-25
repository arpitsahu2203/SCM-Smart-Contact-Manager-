package org.arpitsahu.smc.ServiceImpl;

import org.arpitsahu.smc.Entities.Contact;
import org.arpitsahu.smc.Entities.Users;
import org.arpitsahu.smc.Helper.ResourceNotFoundException;
import org.arpitsahu.smc.Repository.contactRepo;
import org.arpitsahu.smc.Services.contactService;
import org.arpitsahu.smc.Services.imageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class contactServiceImpl implements contactService {

    public final imageService imageService;

    private final contactRepo contactRepo;

    public contactServiceImpl(imageService imageService, contactRepo contactRepo) {
        this.imageService = imageService;
        this.contactRepo = contactRepo;
    }

    @Override
    public Contact saveContact(Contact contact) {
        String contactid= UUID.randomUUID().toString();
        contact.setId(contactid);

        contactRepo.save(contact);
        return contact;
    }

    @Override
    public Contact updateContact(Contact contact) {
        Contact existingContact = contactRepo.findById(contact.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id: " + contact.getId()));

        String oldPublicImageId = existingContact.getPublicImageId();

        existingContact.setName(contact.getName());
        existingContact.setEmail(contact.getEmail());
        existingContact.setPhoneNumber(contact.getPhoneNumber());
        existingContact.setAddress(contact.getAddress());
        existingContact.setDescription(contact.getDescription());
        existingContact.setFavorite(contact.isFavorite());
        existingContact.setInstagramLink(contact.getInstagramLink());
        existingContact.setLinkedinLink(contact.getLinkedinLink());
        existingContact.setTwitterLink(contact.getTwitterLink());
        existingContact.setWebsiteLink(contact.getWebsiteLink());

        boolean imageWasReplaced = contact.getPublicImageId() != null
                && !contact.getPublicImageId().isBlank()
                && !contact.getPublicImageId().equals(oldPublicImageId);

        if (contact.getPicture() != null && !contact.getPicture().isBlank()) {
            existingContact.setPicture(contact.getPicture());
        }
        if (contact.getPublicImageId() != null && !contact.getPublicImageId().isBlank()) {
            existingContact.setPublicImageId(contact.getPublicImageId());
        }

        Contact updatedContact=contactRepo.save(existingContact);
        if(imageWasReplaced){
            imageService.deleteImage(oldPublicImageId);
        }

        return contactRepo.save(existingContact);
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
        imageService.deleteImage(contact.getPublicImageId());
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

    @Override
    public Page<Contact> getByUser(Users user, int page, int size, String sortBy, String direction) {
        Sort sort=direction.equals("desc")? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();
        var pageable=PageRequest.of(page, size, sort);
        return contactRepo.findByUser(user , pageable);
    }
}
