package com.adson.staymanager.mapper;

import com.adson.staymanager.dto.request.UserRequestDTO;
import com.adson.staymanager.dto.response.UserResponseDTO;
import com.adson.staymanager.entity.User;

public class UserMapper {

    public static User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }

    public static UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
    
}
