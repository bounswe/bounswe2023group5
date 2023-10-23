package com.app.gamereview.service;

import com.app.gamereview.dto.request.ChangeUserPasswordRequestDto;
import com.app.gamereview.dto.request.ForgotChangeUserPasswordRequestDto;
import com.app.gamereview.dto.request.RegisterUserRequestDto;
import com.app.gamereview.model.User;
import com.app.gamereview.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthService {

	private final UserRepository userRepository;

	private final ModelMapper modelMapper;

	@Autowired
	public AuthService(UserRepository userRepository, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	public User registerUser(RegisterUserRequestDto registerUserRequestDto) {
		User userToCreate = modelMapper.map(registerUserRequestDto, User.class);
		userToCreate.setDeleted(false);
		userToCreate.setVerified(false);
		userToCreate.setCreatedAt(LocalDateTime.now());
		// role assigning logic will change
		userToCreate.setRole("basic");
		return userRepository.save(userToCreate);
	}

	public Boolean changeUserPassword(ChangeUserPasswordRequestDto passwordRequestDto) {
		Optional<User> optionalUser = userRepository.findById(passwordRequestDto.getUserId());

		if (optionalUser.isEmpty()) {
			return false;
		}

		User user = optionalUser.get();

		if (user.getDeleted()) {
			return false;
		}

		if (!Objects.equals(passwordRequestDto.getCurrentPassword(), user.getPassword())) {
			return false;
		}

		user.setPassword(passwordRequestDto.getNewPassword());
		userRepository.save(user);
		return true;
	}

	public Boolean changeForgotPassword(ForgotChangeUserPasswordRequestDto passwordRequestDto) {
		Optional<User> optionalUser = userRepository.findById(passwordRequestDto.getUserId());

		if (optionalUser.isEmpty()) {
			return false;
		}

		User user = optionalUser.get();

		if (user.getDeleted()) {
			return false;
		}

		user.setPassword(passwordRequestDto.getNewPassword());
		userRepository.save(user);
		return true;
	}

}
