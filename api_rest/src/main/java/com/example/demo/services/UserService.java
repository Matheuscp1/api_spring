package com.example.demo.services;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entities.User;
import com.example.demo.exceptions.NotFoundExpection;
import com.example.demo.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	UserRepository repository;

	private Logger logger = Logger.getLogger(UserService.class.getName());


	public UserService(UserRepository repository) {
		this.repository = repository;
	}
	public List<User> findAll(){
		List<User> users = this.repository.findAll();
		return users;
	}

	public User create(User user) {
		if(user.getEmail().trim() == "")  throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "email cannot be empty");
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		Pbkdf2PasswordEncoder pbkdf2Encoder =
				new Pbkdf2PasswordEncoder(
						"", 8, 185000,
						SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
		encoders.put("pbkdf2", pbkdf2Encoder);
		DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
		passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);

		user.setPassword(passwordEncoder.encode(user.getPassword()));
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
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Finding one user by name " + username + "!");
		var user = repository.findByUsername(username);
		logger.info("Finding one user by name " + user.getEmail() + "!");
		if (user != null) {
			return user;
		} else {
			throw new UsernameNotFoundException("Username " + username + " not found!");
		}
	}

}
