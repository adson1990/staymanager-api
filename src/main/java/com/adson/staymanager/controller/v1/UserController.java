package com.adson.staymanager.controller.v1;

import com.adson.staymanager.dto.request.UserRequestDTO;
import com.adson.staymanager.dto.response.UserResponseDTO;
import com.adson.staymanager.entity.User;
import com.adson.staymanager.mapper.UserMapper;
import com.adson.staymanager.service.UserService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserRequestDTO requestDTO) {

        User user = UserMapper.toEntity(requestDTO);
        User savedUser = userService.create(user, requestDTO.getRoleID());
        UserResponseDTO responseDTO = UserMapper.toDTO(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}