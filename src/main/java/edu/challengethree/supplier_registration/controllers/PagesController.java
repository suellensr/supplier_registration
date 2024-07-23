package edu.challengethree.supplier_registration.controllers;

import edu.challengethree.supplier_registration.DTOs.AuthenticationDTO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PagesController {

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("authenticationDTO", new AuthenticationDTO());
        return "login";
    }
}
