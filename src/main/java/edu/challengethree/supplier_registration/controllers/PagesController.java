package edu.challengethree.supplier_registration.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {

    @GetMapping("/login")
    public String loadLogin() {
        return "login";
    }

//    @GetMapping("/login")
//    public String showLoginForm(Model model) {
//        model.addAttribute("authenticationDTO", new AuthenticationDTO());
//        return "login";
//    }

    @GetMapping("/user-register")
    public String loadUserRegister() {
        return "user-register";
    }

    @GetMapping("/home")
    public String loadHome() {
        return "home";
    }
}
