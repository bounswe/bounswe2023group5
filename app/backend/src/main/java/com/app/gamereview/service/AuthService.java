package com.app.gamereview.service;

import com.app.gamereview.dto.request.RegisterUserRequestDto;
import com.app.gamereview.model.User;
import com.app.gamereview.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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
        Optional<User> sameUsername = userRepository.findByUsernameAndIsDeletedFalse(
                registerUserRequestDto.getUsername());
        Optional<User> sameEmail = userRepository.findByEmailAndIsDeletedFalse(
                registerUserRequestDto.getEmail());

        if(sameUsername.isPresent() || sameEmail.isPresent()){
            // TODO : Add exception handling mechanism and custom exceptions
            return null;
        }

        User userToCreate = modelMapper.map(registerUserRequestDto, User.class);
        userToCreate.setDeleted(false);
        userToCreate.setVerified(false);
        userToCreate.setCreatedAt(LocalDateTime.now());
        // role assigning logic will change
        userToCreate.setRole("basic");
        return userRepository.save(userToCreate);
    }
}
