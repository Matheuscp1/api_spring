package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.User;
import com.example.demo.exceptions.NotFoundExpection;
import com.example.demo.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository repository;
	
	public List<User> findAll(){
		List<User> users = this.repository.findAll();
		return users;
	}
	
	public User create(User user) {
		if(user.getEmail().trim() == "")  throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "email cannot be empty");
		return this.repository.save(user);
	}
	
	public User findById(Long id) {
		return this.repository.findById(id).orElseThrow(() -> new NotFoundExpection("User not found " + id));
	}
	
	public User update(User user) {
		User findUser = this.repository.findById(user.getId()).orElseThrow(() -> new NotFoundExpection("User not found " + user.getId()));
		if(user.getEmail() == null)
		user.setEmail(findUser.getEmail());
		return this.repository.save(user);
	}
	

	public void delete(Long id) {
		User user = findById(id);
		this.repository.delete(user);
	}

}
