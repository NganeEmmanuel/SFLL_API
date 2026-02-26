package com.socialapp.sfll.UserService.mapper;

import com.socialapp.sfll.UserService.dto.UpdateRequest;
import com.socialapp.sfll.UserService.model.User;
import org.springframework.stereotype.Service;

@Service(value = "userMapper")
public class UserMapper implements Mapper<User, UpdateRequest>{
    @Override
    public UpdateRequest toDto(User entity) {
        return null;
    }

    @Override
    public void toEntity(User user, UpdateRequest dto) {
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
    }
}
