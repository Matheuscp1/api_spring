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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping(value = "/users")
@Tag(name = "User", description = "Endpoint for Managing User")
public class UserController{
	
	@Autowired
	UserService service;
	@GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@Operation(summary = "Find all users", description = "Find all users", tags = {"User"},
	responses = {
			@ApiResponse(responseCode = "200", description = "Success",
					content = {
							@Content(mediaType = "application/json",
									array = @ArraySchema(schema = @Schema(implementation = User.class))),
							@Content(mediaType = "application/xml",schema = @Schema(implementation = User.class))
			}),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Error", content = @Content),
			}
	)
	public ResponseEntity<List<User>> findAll(){
		List<User> users = this.service.findAll();
		users.stream().forEach(user-> user.add(linkTo(methodOn(UserController.class).findById(user.getId())).withSelfRel()));
		return ResponseEntity.ok().body(users);
	}
	
	@GetMapping(value = "/{id}")
	@Operation(summary = "Find user by id", description = "Find user by id", tags = {"User"},
	responses = {
			@ApiResponse(responseCode = "200", description = "Success",
					content = {
							@Content(mediaType = "application/json",
									schema = @Schema(implementation = User.class)),
							@Content(mediaType = "application/xml",schema = @Schema(implementation = User.class))
			}),
			@ApiResponse(responseCode = "204", description = "No Content", content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Error", content = @Content),
			}
	)
	public ResponseEntity<User> findById(@PathVariable(value = "id") Long id){
		User user = this.service.findById(id);
		user.add(linkTo(methodOn(UserController.class).findById(id)).withSelfRel());
		return ResponseEntity.ok().body(user);
	}
	
	@PostMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	@Operation(summary = "Create user", description = "Create user", tags = {"User"},
	responses = {
			@ApiResponse(responseCode = "201", description = "Created",
					content = {
							@Content(mediaType = "application/json",
									schema = @Schema(implementation = User.class)),
							@Content(mediaType = "application/xml",schema = @Schema(implementation = User.class))
			}),

			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Error", content = @Content),
			}
	)
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
	@Operation(summary = "Update user", description = "Update user", tags = {"User"},
	responses = {
			@ApiResponse(responseCode = "200", description = "Success",
					content = {
							@Content(mediaType = "application/json",
									schema = @Schema(implementation = User.class)),
							@Content(mediaType = "application/xml",schema = @Schema(implementation = User.class))
			}),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Error", content = @Content),
			}
	)
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
	@Operation(summary = "Delete user", description = "Delete user", tags = {"User"},
	responses = {
			@ApiResponse(responseCode = "204", description = "No content",content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Error", content = @Content),
			}
	)
	public ResponseEntity<Object> delete(@PathVariable(value = "id") Long id) {
		this.service.delete(id);
		return ResponseEntity.noContent().build();
	}
}