package com.app.gamereview.service;

import com.app.gamereview.dto.request.auth.ChangeUserPasswordRequestDto;
import com.app.gamereview.dto.request.auth.ForgotChangeUserPasswordRequestDto;
import com.app.gamereview.dto.request.auth.LoginUserRequestDto;
import com.app.gamereview.dto.request.auth.RegisterUserRequestDto;
import com.app.gamereview.dto.response.auth.LoginUserResponseDto;
import com.app.gamereview.dto.response.user.UserResponseDto;
import com.app.gamereview.exception.BadRequestException;
import com.app.gamereview.exception.ResourceNotFoundException;
import com.app.gamereview.model.ResetCode;
import com.app.gamereview.model.User;
import com.app.gamereview.repository.ResetCodeRepository;
import com.app.gamereview.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.app.gamereview.util.JwtUtil;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

	private final UserRepository userRepository;

	private final ResetCodeRepository resetCodeRepository;

	private final ModelMapper modelMapper;

	@Autowired
	public AuthService(UserRepository userRepository, ResetCodeRepository resetCodeRepository,
			ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.resetCodeRepository = resetCodeRepository;
		this.modelMapper = modelMapper;
	}

	public User registerUser(RegisterUserRequestDto registerUserRequestDto) {
		Optional<User> sameUsername = userRepository
			.findByUsernameAndIsDeletedFalse(registerUserRequestDto.getUsername());
		Optional<User> sameEmail = userRepository.findByEmailAndIsDeletedFalse(registerUserRequestDto.getEmail());

		if (sameUsername.isPresent() || sameEmail.isPresent()) {
			throw new BadRequestException("User with the same information already exists");
		}

		User userToCreate = modelMapper.map(registerUserRequestDto, User.class);
		userToCreate.setIsDeleted(false);
		userToCreate.setVerified(false);
		userToCreate.setCreatedAt(LocalDateTime.now());
		// role assigning logic will change
		userToCreate.setRole("basic");
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(registerUserRequestDto.getPassword());
		userToCreate.setPassword(hashedPassword);
		return userRepository.save(userToCreate);
	}

	public Boolean changeUserPassword(ChangeUserPasswordRequestDto passwordRequestDto, User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String userPassword = user.getPassword();

		if (!passwordEncoder.matches( passwordRequestDto.getCurrentPassword(), userPassword)) {
			return false;
		}
		String hashedPassword = passwordEncoder.encode(passwordRequestDto.getNewPassword());
		user.setPassword(hashedPassword);
		userRepository.save(user);
		return true;
	}

	public Boolean changeForgotPassword(ForgotChangeUserPasswordRequestDto passwordRequestDto, User user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(passwordRequestDto.getNewPassword());
		user.setPassword(hashedPassword);
		userRepository.save(user);
		return true;
	}

	public LoginUserResponseDto loginUser(LoginUserRequestDto loginUserRequestDto) {
		Optional<User> userOptional = userRepository.findByEmailAndIsDeletedFalse(loginUserRequestDto.getEmail());

		if (userOptional.isPresent()) {
			User user = userOptional.get();

			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String storedHashedPassword = user.getPassword();
			String enteredPassword = loginUserRequestDto.getPassword();

			if (passwordEncoder.matches(enteredPassword, storedHashedPassword)) {
				String token = JwtUtil.generateToken(user.getEmail());
				LoginUserResponseDto response = new LoginUserResponseDto();
				response.setUser(new UserResponseDto(user));
				response.setToken(token);
				return response;
			}
			else {
				throw new BadRequestException("Password is wrong");
			}
		}
		else {
			throw new ResourceNotFoundException("User not found");
		}
	}

	public UserResponseDto me(User user) {
        UserResponseDto response = new UserResponseDto(user);
        return response;
	}

	public ResetCode getResetCodeByCode(String code) {
		Optional<ResetCode> resetCodeOptional = resetCodeRepository.findByCode(code);

		return resetCodeOptional.orElse(null);
	}

	public void deleteCodeByUserId(String userId) {
		resetCodeRepository.deleteByUserId(userId);
	}

	public String generateResetCode(String userId) {
		// Check if a reset code exists for the user
		ResetCode existingResetCode = resetCodeRepository.findByUserId(userId);

		// If a reset code exists, delete it
		if (existingResetCode != null) {
			resetCodeRepository.delete(existingResetCode);
		}
		String code = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();

		ResetCode resetCode = new ResetCode(code, userId, new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
		resetCodeRepository.save(resetCode);

		return code;
	}

}
