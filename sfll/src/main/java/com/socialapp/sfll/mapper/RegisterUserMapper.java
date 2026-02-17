package com.socialapp.sfll.mapper;

import com.socialapp.sfll.dto.RegisterUser;
import com.socialapp.sfll.model.User;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserMapper implements Mapper<User, RegisterUser>{
    @Override
    public RegisterUser toDto(User entity) {
        return RegisterUser.builder()
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .username(entity.getUsername())
                .build();
    }

    @Override
    public void toEntity(User user, RegisterUser dto) {
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        // set role in the methode
    }
}
