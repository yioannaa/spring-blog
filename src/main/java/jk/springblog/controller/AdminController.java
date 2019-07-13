package jk.springblog.controller;

import jk.springblog.model.Contact;
import jk.springblog.model.Search;
import jk.springblog.service.ContactService;
import jk.springblog.service.UserService;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class AdminController {

    ContactService contactService;
    UserService userService;


    @Autowired
    public AdminController(ContactService contactService, UserService userService) {
        this.contactService = contactService;
        this.userService = userService;
    }


    @GetMapping("/admin")
    public String adminPanel(Model model){
        model.addAttribute("contacts", contactService.getAllContacts());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("role_admin", userService.getAdminRole());
        model.addAttribute("search", new Search());
        return "admin/tables";
    }
    @GetMapping("/admin/changeStatus/{contact_id}")
    public String changeStatus(@PathVariable Long contact_id, Model model){
        contactService.changeStatus(contact_id);
        model.addAttribute("contacts",contactService.getAllContacts());
        return "admin/tables";
    }

    @GetMapping("/addAdmin/{user_id}")
    public String addAdmin(@PathVariable Long user_id){
        userService.addAdminRole(user_id);
        return "redirect:/admin";

    }

    @GetMapping("/subAdmin/{user_id}")
    public String subAdmin (@PathVariable Long user_id){
        userService.subAdminRole(user_id);
        return "redirect:/admin";
    }

    @GetMapping("/searchContact")
        public String searchContact (@ModelAttribute Search search, Model model){
            List<Contact> contacts = contactService.searchedContact(search.getSearch_pattern());
            model.addAttribute("contacts", contacts);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("role_admin", userService.getAdminRole());
            return "admin/tables";
        }
    }


