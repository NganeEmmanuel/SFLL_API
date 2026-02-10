package com.socialapp.sfll.service;

import com.socialapp.sfll.dto.UpdateRequest;
import com.socialapp.sfll.exceptions.UserNotAuthorizedException;
import com.socialapp.sfll.exceptions.UserNotFoundException;
import com.socialapp.sfll.mapper.Mapper;
import com.socialapp.sfll.model.User;
import com.socialapp.sfll.repository.ORMUserRopository;
import com.socialapp.sfll.utils.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@SuppressWarnings("unused")
public class UserService {
//    @Autowired
//    private UnsafeUserRepository userRepository;

    @Autowired
    private ORMUserRopository ormUserRepository;

    @Autowired
    @Qualifier("userMapper")
    private Mapper<User, UpdateRequest> userMapper;

    @Autowired
    private Validator validator;

    public User register(User user) {
        // Implement registration logic here (e.g., save user to database
        user.setRole("USER");
        return ormUserRepository.save(user); // Return the registered user
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
