package com.example.data;

import com.example.model.User;

public interface UserRepository {
    boolean save(User user);
}
