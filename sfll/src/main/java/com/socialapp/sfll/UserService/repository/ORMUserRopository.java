package com.socialapp.sfll.UserService.repository;

import com.socialapp.sfll.UserService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ORMUserRopository extends JpaRepository<User, Integer> {
    // SELECT * FROM users WHER username = username AND password = password;
    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

}
