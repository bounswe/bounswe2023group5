package com.app.gamereview.service;

import com.app.gamereview.dto.request.LoginUserRequestDto;
import com.app.gamereview.dto.request.RegisterUserRequestDto;
import com.app.gamereview.dto.response.LoginUserResponseDto;
import com.app.gamereview.dto.response.UserResponseDto;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.User;
import com.app.gamereview.repository.UserRepository;
import com.app.gamereview.service.AuthService;
import com.app.gamereview.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    public void testRegisterUser() {
        RegisterUserRequestDto request = new RegisterUserRequestDto();
        request.setUsername("testUser");
        request.setPassword("testPassword");
        request.setEmail("test@example.com");

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setEmail("test@example.com");

        when(userRepository.findByUsernameAndIsDeletedFalse(request.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmailAndIsDeletedFalse(request.getEmail())).thenReturn(Optional.empty());
        when(modelMapper.map(request, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        User registeredUser = authService.registerUser(request);

        assertNotNull(registeredUser);
        assertEquals("testUser", registeredUser.getUsername());
        assertEquals("testPassword", registeredUser.getPassword());
        assertEquals("test@example.com", registeredUser.getEmail());
    }

    @Test
    public void testLoginUser() {
        LoginUserRequestDto request = new LoginUserRequestDto();
        request.setEmail("test@example.com");
        request.setPassword("testPassword");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("testPassword");

        when(userRepository.findByEmailAndIsDeletedFalse(request.getEmail())).thenReturn(Optional.of(user));

        LoginUserResponseDto response = authService.loginUser(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("test@example.com", response.getUser().getEmail());
    }

    @Test
    public void testLoginUserInvalidPassword() {
        LoginUserRequestDto request = new LoginUserRequestDto();
        request.setEmail("test@example.com");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("testPassword");

        when(userRepository.findByEmailAndIsDeletedFalse(request.getEmail())).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> authService.loginUser(request));
    }

    @Test
    public void testLoginUserNotFound() {
        LoginUserRequestDto request = new LoginUserRequestDto();
        request.setEmail("notfound@example.com");
        request.setPassword("testPassword");

        when(userRepository.findByEmailAndIsDeletedFalse(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.loginUser(request));
    }
}