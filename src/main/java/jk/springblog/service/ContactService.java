package jk.springblog.service;

import jk.springblog.model.Contact;
import jk.springblog.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void addContact(Contact contact){
        contactRepository.save(contact);
    }

    public List<Contact>getAllContacts(){
        return contactRepository.findAll();
    }

    public void changeStatus(Long contact_id){
        Contact contact = contactRepository.getOne(contact_id);
        contact.setStatus(!contact.isStatus());
        contactRepository.save(contact);
    }

    public List<Contact>searchedContact (String pattern){
        pattern = "%" + pattern +"%";
        return contactRepository.findAllByEmailLikeOrMessageLikeOrNameLikeOrPhoneLike(pattern,pattern,pattern,pattern);
    }
}
