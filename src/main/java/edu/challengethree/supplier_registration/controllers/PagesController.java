package edu.challengethree.supplier_registration.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PagesController {

    @GetMapping("/login")
    public String loadLogin() {
        return "login";
    }

    @GetMapping("/user-register")
    public String loadUserRegister() {
        return "user-register";
    }

    @GetMapping("/home")
    public String loadHome() {
        return "home";
    }

    @GetMapping("/view-supplier/{id}")
    public String loadViewSupplier(@PathVariable String id, Model model) {
        model.addAttribute("supplierId", id);
        return "view-supplier";
    }

    @GetMapping("/supplier-register")
    public String loadSupplierRegister() {
        return "supplier-register";
    }

    @GetMapping("/edit-supplier/{id}")
    public String editSupplier(@PathVariable String id, Model model) {
        model.addAttribute("supplierId", id);
        return "edit-supplier";
    }
}
