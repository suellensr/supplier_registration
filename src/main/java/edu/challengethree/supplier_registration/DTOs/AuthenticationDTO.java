package edu.challengethree.supplier_registration.DTOs;

import javax.validation.constraints.Email;

public class AuthenticationDTO {

    @Email
    private String email;
    private String password;

    public @Email String getEmail() {
        return email;
    }

    public void setEmail(@Email String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
