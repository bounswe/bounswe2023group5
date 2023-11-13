package com.app.gamereview.service;

import com.app.gamereview.dto.request.user.GetAllUsersFilterRequestDto;
import com.app.gamereview.model.User;
import com.app.gamereview.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private org.modelmapper.ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getUsername());
    }

    @Test
    void testGetAllUsersWithFilter() {
        User user = new User();
        user.setUsername("testUser");
        GetAllUsersFilterRequestDto filter = new GetAllUsersFilterRequestDto();
        filter.setUsername("testUser");
        when(mongoTemplate.find(any(Query.class), eq(User.class))).thenReturn(Collections.singletonList(user));

        List<User> result = userService.getAllUsers(filter);

        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getUsername());
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        User result = userService.getUserById("1");

        assertEquals("testUser", result.getUsername());
    }

    @Test
    void testDeleteUserById() {
        User user = new User();
        user.setUsername("testUser");
        user.setIsDeleted(false);
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(mongoTemplate.updateFirst(any(Query.class), any(), eq(User.class))).thenReturn(null);

        Boolean result = userService.deleteUserById("1");

        assertTrue(result);
    }

    @Test
    void testGetUserByEmail() {
        User user = new User();
        user.setEmail("test@test.com");
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        User result = userService.getUserByEmail("test@test.com");

        assertEquals("test@test.com", result.getEmail());
    }
}
