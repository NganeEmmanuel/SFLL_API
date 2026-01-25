package com.socialapp.sfll.service;

import com.socialapp.sfll.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public User register(User user) {
        // Implement registration logic here (e.g., save user to database)
        return user; // Return the registered user
    }
}
