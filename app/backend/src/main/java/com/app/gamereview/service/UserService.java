package com.app.gamereview.service;

import com.app.gamereview.dto.request.user.GetAllUsersFilterRequestDto;
import com.app.gamereview.model.User;
import com.app.gamereview.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	private final UserRepository userRepository;

	private final MongoTemplate mongoTemplate;

	private final ModelMapper modelMapper;

	@Autowired
	public UserService(UserRepository userRepository, MongoTemplate mongoTemplate, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.mongoTemplate = mongoTemplate;
		this.modelMapper = modelMapper;
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public List<User> getAllUsers(GetAllUsersFilterRequestDto filter) {
		Query query = new Query();
		if (filter.getId() != null) {
			query.addCriteria(Criteria.where("_id").is(filter.getId()));
		}
		if (filter.getUsername() != null) {
			query.addCriteria(Criteria.where("username").is(filter.getUsername()));
		}
		if (filter.getIsDeleted() != null) {
			query.addCriteria(Criteria.where("isDeleted").is(filter.getIsDeleted()));
		}

		return mongoTemplate.find(query, User.class);
	}

	public User getUserById(String id) {
		Optional<User> getResult = userRepository.findById(id);

		return getResult.orElse(null);
	}

	public Boolean deleteUserById(String id) {
		Optional<User> findResult = userRepository.findById(id);

		// TODO : Delete related data of the user such as profile, achievements etc.

		if (findResult.isPresent() && !findResult.get().getIsDeleted()) {
			Query query = new Query(Criteria.where("_id").is(id));
			Update update = new Update().set("isDeleted", true);
			mongoTemplate.updateFirst(query, update, User.class);
			return true;
		}
		return false;
	}

	public User getUserByEmail(String email) {
		Optional<User> getResult = userRepository.findByEmail(email);

		return getResult.orElse(null);
	}

}
