package com.app.gamereview.service;

import com.app.gamereview.dto.request.RegisterUserRequestDto;
import com.app.gamereview.dto.request.LoginUserRequestDto;
import com.app.gamereview.dto.response.LoginUserResponseDto;
import com.app.gamereview.model.User;
import com.app.gamereview.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.app.gamereview.util.JwtUtil;

import java.time.LocalDateTime;
import java.util.*;

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

    public LoginUserResponseDto loginUser(LoginUserRequestDto loginUserRequestDto){
        // Retrieve the user by email from the database
        Optional<User> userOptional = userRepository.findByEmail(loginUserRequestDto.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            String hashedPassword = user.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            // Check if the provided password matches the stored password
            if (passwordEncoder.matches(loginUserRequestDto.getPassword(), hashedPassword)) {
                // Passwords match, generate a JWT token
                String token = JwtUtil.generateToken(user.getEmail());
                LoginUserResponseDto response = new LoginUserResponseDto();
                response.setUser(user);
                response.setToken(token);
                // Return the JWT token in the response
                return response;
            }
        }

        // If the email and password do not match, return an unauthorized response
        return null;
    }
}
