package edu.challengethree.supplier_registration.services.impl;

import edu.challengethree.supplier_registration.DTOs.UserCreationDTO;
import edu.challengethree.supplier_registration.DTOs.UserDTO;
import edu.challengethree.supplier_registration.exceptions.DifferentPasswordsException;
import edu.challengethree.supplier_registration.exceptions.EmailAlreadyRegisteredException;
import edu.challengethree.supplier_registration.model.entities.User;
import edu.challengethree.supplier_registration.repositories.UserRepository;
import edu.challengethree.supplier_registration.services.interfaces.UserService;
import edu.challengethree.supplier_registration.services.utils.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO createUser(UserCreationDTO userCreationDTO) throws DifferentPasswordsException, EmailAlreadyRegisteredException {
        if (userRepository.findByEmail(userCreationDTO.getEmail()) != null) {
            throw new EmailAlreadyRegisteredException("This email is already registered");
        }
        if (!userCreationDTO.getPassword().equals(userCreationDTO.getConfirmPassword())) {
            throw new DifferentPasswordsException("Passwords do not match");
        }
        User user = new User();
        user.setUsername(userCreationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userCreationDTO.getPassword()));

        userRepository.save(user);

        return UserMapper.userToUserDTO(user);
    }
}
