package com.example.services;

import com.example.model.User;

public interface UserService {

    User createUser(String firstname, String lastname, String email, String password, String repeatePassword) throws IllegalArgumentException, UserServiceException;
    // create
    // get
    // update
    // delete
}
