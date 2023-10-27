package com.app.gamereview.service;

import com.app.gamereview.dto.request.*;
import com.app.gamereview.dto.response.LoginUserResponseDto;
import com.app.gamereview.dto.response.UserResponseDto;
import com.app.gamereview.model.User;
import com.app.gamereview.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.app.gamereview.util.JwtUtil;

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
		Optional<User> sameUsername = userRepository
			.findByUsernameAndIsDeletedFalse(registerUserRequestDto.getUsername());
		Optional<User> sameEmail = userRepository.findByEmailAndIsDeletedFalse(registerUserRequestDto.getEmail());

		if (sameUsername.isPresent() || sameEmail.isPresent()) {
			// TODO : will add exception handling mechanism and custom exceptions
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

	public LoginUserResponseDto loginUser(LoginUserRequestDto loginUserRequestDto) {
		Optional<User> userOptional = userRepository.findByEmailAndIsDeletedFalse(loginUserRequestDto.getEmail());

		if (userOptional.isPresent()) {
			User user = userOptional.get();

			String password = user.getPassword();
			if (password.equals(loginUserRequestDto.getPassword())) {
				String token = JwtUtil.generateToken(user.getEmail());
				LoginUserResponseDto response = new LoginUserResponseDto();
				response.setUser(new UserResponseDto(user));
				response.setToken(token);
				return response;
			}
		}

		return null;
	}

	public UserResponseDto me(MeRequestDto meRequestDto) {
		String token = meRequestDto.getToken();
		String email = JwtUtil.extractSubject(token);
		Optional<User> userOptional = userRepository.findByEmailAndIsDeletedFalse(email);

		if (userOptional.isPresent()) {
			User user = userOptional.get();
			UserResponseDto response = new UserResponseDto(user);
			return response;
		}
		return null;

	}



}
