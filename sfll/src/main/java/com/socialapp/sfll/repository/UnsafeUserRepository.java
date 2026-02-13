package com.socialapp.sfll.repository;

import com.socialapp.sfll.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public class UnsafeUserRepository {
    @PersistenceContext
    EntityManager entityManager;

    public User save(User user) {
        String sql = "INSERT INTO users (first_name, last_name, username, email, password) VALUES ('"
                + user.getFirstName() + "', '"
                + user.getLastName() + "', '"
                + user.getUsername() + "', '"
                + user.getEmail() + "', '"
                + user.getPassword() + "')";

        var id = entityManager.createNativeQuery(sql).executeUpdate();
        user.setId((long) id);
        return user;
    }

    public List<User> login(String username, String password) {
        String sql = """
            SELECT * FROM users
            WHERE username = :username
            AND password = :password
        """;

        return entityManager.createNativeQuery(sql, User.class)
                .setParameter("username", username)
                .setParameter("password", password).getResultList();
    }

//    private User findById(int id) {
//        String sql = "SELECT * FROM users WHERE id ="+id;
//        return (User) entityManager.createNativeQuery(sql, User.class).getSingleResult();
//    }

}
