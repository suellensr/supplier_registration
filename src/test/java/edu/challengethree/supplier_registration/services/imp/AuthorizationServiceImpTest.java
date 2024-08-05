package edu.challengethree.supplier_registration.services.imp;

import edu.challengethree.supplier_registration.exceptions.EmailAlreadyRegisteredException;
import edu.challengethree.supplier_registration.model.entities.User;
import edu.challengethree.supplier_registration.repositories.UserRepository;
import edu.challengethree.supplier_registration.services.impl.AuthorizationServiceImp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorizationServiceImpTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthorizationServiceImp authorizationService;

    @Test
    @DisplayName("Load an user by Username successfully")
    public void loadUserSuccess() {

        // Arrange
        String username = "test@example.com";

        User user = new User();
        user.setUsername("test@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(username)).thenReturn(user);

        // Act
        UserDetails foundUser = authorizationService.loadUserByUsername(username);

        // Assert
        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
        assertEquals(user.getPassword(), foundUser.getPassword());
    }

    @Test
    @DisplayName("Load an user by Username throw UsernameNotFoundException")
    public void loadUserException() {

        // Arrange
        String username = "test@example.com";

        when(userRepository.findByEmail(username)).thenReturn(null);

        // Act & Assert
        assertThatThrownBy(() -> authorizationService.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");
    }
}
