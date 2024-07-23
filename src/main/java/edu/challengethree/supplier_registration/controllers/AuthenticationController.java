package edu.challengethree.supplier_registration.controllers;

import edu.challengethree.supplier_registration.DTOs.AuthenticationDTO;
import edu.challengethree.supplier_registration.DTOs.LoginResponseDTO;
import edu.challengethree.supplier_registration.DTOs.UserDTO;
import edu.challengethree.supplier_registration.exceptions.EmailAlreadyRegisteredException;
import edu.challengethree.supplier_registration.model.entities.User;
import edu.challengethree.supplier_registration.security.TokenService;
import edu.challengethree.supplier_registration.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin("*")
@RequestMapping("auth")
@Validated
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@ModelAttribute @Valid AuthenticationDTO authenticationDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(), authenticationDTO.getPassword());
        var authenticated = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) authenticated.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserDTO userDTO) {
        try {
            UserDTO createdUser = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (EmailAlreadyRegisteredException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
