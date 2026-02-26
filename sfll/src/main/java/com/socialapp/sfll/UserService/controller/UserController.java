package com.socialapp.sfll.UserService.controller;

import com.socialapp.sfll.UserService.dto.LoginUser;
import com.socialapp.sfll.UserService.dto.RegisterUser;
import com.socialapp.sfll.UserService.dto.UpdateRequest;
import com.socialapp.sfll.UserService.model.User;
import com.socialapp.sfll.AuthService.security.auth.jwt.dto.JwtToken;
import com.socialapp.sfll.UserService.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public JwtToken register(@RequestBody RegisterUser user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public JwtToken login(@RequestBody @Valid LoginUser user) {
        return userService.loginWithOrm(user);
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
