package com.socialapp.sfll.UserService.annotation.AnnotaionImpl;


import com.socialapp.sfll.UserService.annotation.Encrypted;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Field;

public class EncryptionListener {

    private static final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    @PrePersist
    @PreUpdate
    public void encrypt(Object entity) {

        for (Field field : entity.getClass().getDeclaredFields()) {

            if (field.isAnnotationPresent(Encrypted.class)) {

                field.setAccessible(true);

                try {

                    Object value = field.get(entity);

                    if (value != null) {

                        String raw = value.toString();

                        // Prevent double-hashing
                        String hashed = encoder.encode(raw);

                        field.set(entity, hashed);
                    }

                } catch (IllegalAccessException e) {
                    throw new RuntimeException(
                            "Encryption failed for field: " + field.getName(), e
                    );
                }
            }
        }
    }
}