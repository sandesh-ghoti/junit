package com.example.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.data.UserRepository;
import com.example.model.User;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    String firstname;
    String lastname;
    String email;
    String password;
    String repeatePassword;

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        firstname = "test";
        lastname = "test";
        email = "test@test.com";
        password = "test123#";
        repeatePassword = "test123#";
        MockitoAnnotations.openMocks(this);

    }

    @Test
    @DisplayName("Throws exception when fields are not valid")
    void createUserWhenFieldsAreNotValid() {
        // Arrange
        firstname = "";
        String expectedExceptionMessag = "All fields are required";

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(firstname, lastname, email, password, repeatePassword);
        }, "IllegalArgumentException should be thrown");

        // Assert
        assertEquals(expectedExceptionMessag, thrown.getMessage());
    }

    @Test
    @DisplayName("Throws exception when fields are not valid")
    void createUserWhenPasswordsNotMatched() {

        // Arrange
        repeatePassword = "#";
        String expectedExceptionMessag = "Passwords do not match";

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(firstname, lastname, email, password, repeatePassword);
        }, "IllegalArgumentException should be thrown");
        assertEquals(expectedExceptionMessag, thrown.getMessage());
    }

    @Test
    @DisplayName("create user when input is valid")
    void createUser() throws IllegalArgumentException, UserServiceException {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(true);
        // Act
        User user = userService.createUser(firstname, lastname, email, password, repeatePassword);

        // Assert
        assertNotNull(user, "user should not be null");
        assertEquals(firstname, user.getFirstname(), "firstname should be equal");
        assertEquals(lastname, user.getLastname(), "lastname should be equal");
        assertEquals(email, user.getEmail(), "email should be equal");
        assertEquals(password, user.getPassword(), "password should be equal");
        assertNotNull(user.getId(), "id should not be null");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("should throw exception when save() method thows")

    void createUserWhenSaveThrows() throws IllegalArgumentException, UserServiceException {
        // Arrange
        // mock userRepository to throw exception
        when(userRepository.save(any(User.class))).thenThrow(RuntimeException.class);
        // Act & Assert
        assertThrows(UserServiceException.class, () -> {
            userService.createUser(firstname, lastname, email, password, repeatePassword);
        }, "UserServiceException should be thrown");
    }

    @Test
    @DisplayName("should throw exception when sendEmail() throws")
    void sendEmailThrowException() {
        when(userRepository.save(any(User.class))).thenReturn(true);

        // mock EmailService to throw exception for void sendEmail Method
        doThrow(EmailServiceException.class)
               .when(emailService)
               .sendEmail(any(User.class));

        assertThrows(UserServiceException.class, () -> {
            userService.createUser(firstname, lastname, email, password, repeatePassword);
        }, "should have thown UserServiceException");
    }

    @Test
    @DisplayName("should have called sendEmail() once for valid input")
    void callSendEmailOnce(){
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(true);

        doCallRealMethod().when(emailService).sendEmail(any(User.class));

        // Act
        userService.createUser(firstname, lastname, email, password, repeatePassword);

        //Assert
        verify(emailService, times(1)).sendEmail(any(User.class));

    }
}
