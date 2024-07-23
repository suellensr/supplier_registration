package edu.challengethree.supplier_registration.services.interfaces;

import edu.challengethree.supplier_registration.DTOs.UserDTO;
import edu.challengethree.supplier_registration.exceptions.DifferentPasswordsException;
import edu.challengethree.supplier_registration.exceptions.EmailAlreadyRegisteredException;

public interface UserService {
    UserDTO createUser(UserDTO userDTO) throws EmailAlreadyRegisteredException, DifferentPasswordsException;
}
