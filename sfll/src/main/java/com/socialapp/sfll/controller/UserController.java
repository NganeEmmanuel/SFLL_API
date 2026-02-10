package com.socialapp.sfll.controller;

import com.socialapp.sfll.dto.UpdateRequest;
import com.socialapp.sfll.model.User;
import com.socialapp.sfll.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/private/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return userService.loginWithOrm(user.getUsername(), user.getPassword());
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public User update(@RequestBody @Valid UpdateRequest user) {
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> getUser(@RequestBody User user) {
        return userService.getUsers(user);
    }
}
