package com.app.gamereview.service;

import com.app.gamereview.dto.GetAllUsersFilterDto;
import com.app.gamereview.dto.request.RegisterUserRequestDto;
import com.app.gamereview.model.User;
import com.app.gamereview.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(
            UserRepository userRepository,
            ModelMapper modelMapper
    )
    {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllUsers(GetAllUsersFilterDto filter) {
        User exampleUser = new User();

        if (filter.username != null) {
            exampleUser.username = filter.username;
        }

        if (filter.isDeleted != null) {
            exampleUser.isDeleted = filter.isDeleted;
        }

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("id")
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<User> example = Example.of(exampleUser, matcher);

        return userRepository.findAll(example);
    }
    public Optional<User> getUserById(UUID userId) {
        return userRepository.findById(userId);
    }

}
