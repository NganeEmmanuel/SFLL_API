package com.socialapp.sfll.service;

import com.socialapp.sfll.annotation.RequireRole;
import com.socialapp.sfll.dto.RegisterUser;
import com.socialapp.sfll.dto.UpdateRequest;
import com.socialapp.sfll.exceptions.UserNotAuthorizedException;
import com.socialapp.sfll.exceptions.UserNotFoundException;
import com.socialapp.sfll.mapper.Mapper;
import com.socialapp.sfll.model.User;
import com.socialapp.sfll.repository.ORMUserRopository;
import com.socialapp.sfll.security.auth.AuthUser;
import com.socialapp.sfll.security.auth.jwt.JwtTokenProvider;
import com.socialapp.sfll.security.auth.jwt.dto.JwtToken;
import com.socialapp.sfll.utils.validator.Validator;
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

    private final Validator validator;

    @Transactional
    public JwtToken register(RegisterUser registerUser) {
        // Implement registration logic here (e.g., save user to database

        User user = User.builder().role("USER").build();
        RegisterUserMapper.toEntity(user, registerUser);
        ormUserRepository.save(user); // Return the registered user
        AuthUser authUser = new AuthUser(user.getId(), user.getUsername(), Set.of(user.getRole()));
        return  jwtTokenProvider.generateTokens(authUser);

    }

    public User loginWithOrm(String username, String password) {
        // check for blank and empty fields
        validator.validate(username);
        validator.validate(password);
        // find user by username and password using ORM repository
        var user  = ormUserRepository.findByUsernameAndPassword(username, password);
        // check if user is found, else throw UserNotFoundException
        if(user == null) {
            throw new UserNotFoundException("Bad credentials");
        }
        // return the found user
        return user;

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
