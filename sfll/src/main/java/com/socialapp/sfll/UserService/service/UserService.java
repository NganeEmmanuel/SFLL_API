package com.socialapp.sfll.UserService.service;

import com.socialapp.sfll.UserService.annotation.RequireRole;
import com.socialapp.sfll.UserService.dto.LoginUser;
import com.socialapp.sfll.UserService.dto.RegisterUser;
import com.socialapp.sfll.UserService.dto.UpdateRequest;
import com.socialapp.sfll.AuthService.exception.UserNotAuthorizedException;
import com.socialapp.sfll.UserService.exception.UserNotFoundException;
import com.socialapp.sfll.UserService.mapper.Mapper;
import com.socialapp.sfll.UserService.model.User;
import com.socialapp.sfll.UserService.repository.ORMUserRopository;
import com.socialapp.sfll.AuthService.security.auth.AuthUser;
import com.socialapp.sfll.AuthService.security.auth.jwt.JwtTokenProvider;
import com.socialapp.sfll.AuthService.security.auth.jwt.dto.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


@Service
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class UserService {
//    @Autowired
//    private UnsafeUserRepository userRepository;

    private final ORMUserRopository ormUserRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final Mapper<User, UpdateRequest> userMapper;

    private final Mapper<User, RegisterUser> RegisterUserMapper;

    @Transactional
    public JwtToken register(RegisterUser registerUser) {
        // Implement registration logic here (e.g., save user to database

        User user = User.builder().role("USER").build();
        RegisterUserMapper.toEntity(user, registerUser);
        ormUserRepository.save(user); // Return the registered user
        AuthUser authUser = new AuthUser(user.getId(), user.getUsername(), Set.of(user.getRole()));
        return  jwtTokenProvider.generateTokens(authUser);

    }

    public JwtToken loginWithOrm(LoginUser user) {
        // find user by username and password using ORM repository
        var optionalUser  = ormUserRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        // check if user is found, else throw UserNotFoundException
        var dbUser = optionalUser.orElseThrow(() -> new UserNotFoundException("Invalid credentials"));
        // generate tokens from user
        AuthUser authUser = AuthUser.builder()
                .userId(dbUser.getId())
                .username(user.getUsername())
                .roles(Set.of(dbUser.getRole()))
                .build();

        return jwtTokenProvider.generateTokens(authUser);

    }

    @RequireRole("ADMIN")
    public User updateUser(UpdateRequest request) {
        // find user by id
        var dbuser = ormUserRepository.findById(request.getId());
        // check if user is empty
        if(dbuser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        var user = dbuser.get();

        userMapper.toEntity(user, request);

        // save updated user
        try {
            return ormUserRepository.save(user);
        }catch(Exception e) {
            throw new RuntimeException("Error occurred while updating user: ");
        }
    }

    @Transactional
    public void exc(){
        updateUser(new UpdateRequest());
        // other methods 1
        // other methods 2
    }

    public List<User> getUsers(User user) {
        // check if user is admin
        var dbUser = ormUserRepository.findById(user.getId());
        if(dbUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        }

        if(!dbUser.get().getRole().equals("ADMIN")) {
            throw new UserNotAuthorizedException("Not authorized to access this resource");
        }

        return ormUserRepository.findAll();
    }


    //    public List<User> login(String username, String password) {
//
//        // Implement login logic here (e.g., verify username and password)
//        // For simplicity, returning a dummy user
//        validator.validate(username);
//        validator.validate(password);
//        var user = userRepository.login(username, password);
//
//        if (user == null || user.isEmpty()) {
//            throw new UserNotFoundException("Bad credentials");
//        }
//
//        return user;
//    }
}
