package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.User;
import com.example.demo.services.UserService;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping(value = "/users")
public class UserController{
	
	@Autowired
	UserService service;
	@GetMapping
	public ResponseEntity<List<User>> findAll(){
		User user = new User("Matheus", "matheus@gmail.com", "1234");
		List<User> users = this.service.findAll();
		return ResponseEntity.ok().body(users);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<User> findById(@PathVariable(value = "id") Long id){
		User user = this.service.findById(id);
		return ResponseEntity.ok().body(user);
	}
	
	@PostMapping
	public ResponseEntity<User>  create(@RequestBody(required = true) User user) {
		User newUser = this.service.create(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}
	
	@PutMapping
	public User update(@RequestBody(required = true) User user) {
		if(user.getId() == null) throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id required");
		return this.service.update(user);
	}
	
	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable(value = "id") Long id) {
		this.service.delete(id);
	}
}