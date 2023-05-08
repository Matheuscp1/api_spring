package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
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
	@GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<User>> findAll(){
		List<User> users = this.service.findAll();
		users.stream().forEach(user-> user.add(linkTo(methodOn(UserController.class).findById(user.getId())).withSelfRel()));
		return ResponseEntity.ok().body(users);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<User> findById(@PathVariable(value = "id") Long id){
		User user = this.service.findById(id);
		user.add(linkTo(methodOn(UserController.class).findById(id)).withSelfRel());
		return ResponseEntity.ok().body(user);
	}
	
	@PostMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<User>  create(@RequestBody(required = true) User user) {
		if(user.getEmail() == null) throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "email required");
		User newUser = this.service.create(user);
		user.add(linkTo(methodOn(UserController.class).findById(newUser.getId())).withSelfRel());
		user.add(linkTo(methodOn(UserController.class)
	                .findAll()).withRel(IanaLinkRelations.COLLECTION));
		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}
	
	@PutMapping(value = "/{id}",
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<User> update(@RequestBody(required = true) User user, @PathVariable(value = "id") Long id) {
		//if(user.getId() == null) throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id required");
		user.setId(id);
		this.service.update(user);
		user.add(linkTo(methodOn(UserController.class).findById(id)).withSelfRel());
		user.add(linkTo(methodOn(UserController.class)
                .findAll()).withRel(IanaLinkRelations.COLLECTION));
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Object> delete(@PathVariable(value = "id") Long id) {
		this.service.delete(id);
		return ResponseEntity.noContent().build();
	}
}