package com.app.gamereview.service;

import com.app.gamereview.dto.request.RegisterUserRequestDto;
import com.app.gamereview.model.User;
import com.app.gamereview.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthService(
            UserRepository userRepository,
            ModelMapper modelMapper
    )
    {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }
    public User registerUser(RegisterUserRequestDto registerUserRequestDto){
        User userToCreate = modelMapper.map(registerUserRequestDto, User.class);
        userToCreate.isDeleted = false;
        userToCreate.isVerified = false;
        userToCreate.createdAt = LocalDateTime.now();
        // role assigning logic will change
        userToCreate.role = "basic";
        return userRepository.save(userToCreate);
    }
}
