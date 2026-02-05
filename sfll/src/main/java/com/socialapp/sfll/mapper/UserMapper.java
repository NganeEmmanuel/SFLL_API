package com.socialapp.sfll.mapper;

import com.socialapp.sfll.dto.UpdateRequest;
import com.socialapp.sfll.model.User;
import org.springframework.stereotype.Service;

@Service(value = "userMapper")
public class UserMapper implements Mapper<User, UpdateRequest>{
    @Override
    public void toDto(User entity) {
    }

    @Override
    public void toEntity(User user, UpdateRequest dto) {
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
    }
}
