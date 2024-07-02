package edu.challengethree.supplier_registration.services.utils;

import edu.challengethree.supplier_registration.DTOs.UserDTO;
import edu.challengethree.supplier_registration.model.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getUsername());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }
}
