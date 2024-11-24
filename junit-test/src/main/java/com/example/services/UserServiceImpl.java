package com.example.services;

import com.example.data.UserRepository;
import com.example.model.User;

public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService= emailService;
    }

    @Override
    public User createUser(String firstname, String lastname, String email, String password,
            String repeatePassword) throws IllegalArgumentException, UserServiceException {
        if (password != repeatePassword) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (firstname == null || firstname.trim().length() == 0 || lastname == null
                || lastname.trim().length() == 0 || email == null || email.trim().length() == 0
                || password == null || password.trim().length() == 0) {
            throw new IllegalArgumentException("All fields are required");
        }

        User user=new User(firstname, lastname, email, password);
        boolean isUserCreated=false;

        try {
            isUserCreated=userRepository.save(user);
        } catch (RuntimeException e) {
            throw new UserServiceException(e.getMessage());
        }

        if(!isUserCreated){
            throw new UserServiceException("User already exists");
        }

        // now send email for user creation success
        try {
            emailService.sendEmail(user);
        } catch (RuntimeException e) {
            throw new UserServiceException(e.getMessage());
        }

        return user;
    }

}
