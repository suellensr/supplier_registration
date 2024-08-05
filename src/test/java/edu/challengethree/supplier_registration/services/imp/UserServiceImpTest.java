package edu.challengethree.supplier_registration.services.imp;

import edu.challengethree.supplier_registration.DTOs.UserCreationDTO;
import edu.challengethree.supplier_registration.DTOs.UserDTO;
import edu.challengethree.supplier_registration.exceptions.DifferentPasswordsException;
import edu.challengethree.supplier_registration.exceptions.EmailAlreadyRegisteredException;
import edu.challengethree.supplier_registration.model.entities.User;
import edu.challengethree.supplier_registration.repositories.UserRepository;
import edu.challengethree.supplier_registration.services.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImpTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Should create an user successfully")
    public void createUserSuccess() {

        // Arrange
        UserCreationDTO userCreationDTO = new UserCreationDTO();
        userCreationDTO.setEmail("test@example.com");
        userCreationDTO.setPassword("password");
        userCreationDTO.setConfirmPassword("password");

        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        User user = new User();
        user.setUsername("test@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserDTO createdUser = userService.createUser(userCreationDTO);

        // Assert
        assertThat(createdUser)
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    @DisplayName("Should throw EmailAlreadyRegisteredException when email already exists")
    public void createUserEmailException() {

        // Arrange
        UserCreationDTO userCreationDTO = new UserCreationDTO();
        userCreationDTO.setEmail("test@example.com");
        userCreationDTO.setPassword("password");
        userCreationDTO.setConfirmPassword("password");


        User user = new User();
        user.setUsername("test@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(userCreationDTO.getEmail())).thenReturn(user);

        // Act and Assert
        assertThatThrownBy(() -> userService.createUser(userCreationDTO))
                .isInstanceOf(EmailAlreadyRegisteredException.class)
                .hasMessageContaining("This email is already registered");
    }

    @Test
    @DisplayName("Should throw DifferentPasswordsException when order already exists")
    public void createUserPasswordlException() {

        // Arrange
        UserCreationDTO userCreationDTO = new UserCreationDTO();
        userCreationDTO.setEmail("test@example.com");
        userCreationDTO.setPassword("password");
        userCreationDTO.setConfirmPassword("password2");

        // Act and Assert
        assertThatThrownBy(() -> userService.createUser(userCreationDTO))
                .isInstanceOf(DifferentPasswordsException.class)
                .hasMessageContaining("Passwords do not match");
    }
}