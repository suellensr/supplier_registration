package edu.challengethree.supplier_registration.DTOs;

import javax.validation.constraints.Email;

public class UserCreationDTO {

    @Email
    private String email;
    private String password;
    private String confirmPassword;

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
