package com.socialapp.sfll.service;

import com.socialapp.sfll.exceptions.UserNotFoundException;
import com.socialapp.sfll.model.User;
import com.socialapp.sfll.repository.ORMUserRopository;
import com.socialapp.sfll.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
//    @Autowired
//    private UnsafeUserRepository userRepository;

    @Autowired
    private ORMUserRopository ormUserRepository;

    @Autowired
    private Validator validator;

    public User register(User user) {
        // Implement registration logic here (e.g., save user to database
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
