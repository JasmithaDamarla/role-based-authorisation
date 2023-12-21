package com.authorize.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.authorize.exceptions.SignUpFailedException;
import com.authorize.exceptions.UserNotFoundException;
import com.authorize.model.dto.UserDTO;
import com.authorize.service.interfaces.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/signUp")
	public ResponseEntity<UserDTO> signUpUser(@Valid @RequestBody UserDTO userDto) throws SignUpFailedException {
		log.info("user is getting created");
		return new ResponseEntity<>(userService.signUpUser(userDto), HttpStatus.CREATED);
		
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserDTO> updateUserScoreById(@PathVariable int id,@Valid @RequestBody UserDTO userDto) throws UserNotFoundException {
		userDto.setId(id);
		log.info("User info getting updated");
		return new ResponseEntity<>(userService.update(userDto), HttpStatus.ACCEPTED);
	}

	@GetMapping
	public List<UserDTO> getAllUsers() {
		log.info("fetching all users from service");
		return userService.getUsers();
	}

	@GetMapping("/{userName}")
	public ResponseEntity<UserDTO> viewUserByName(@PathVariable String userName) throws UserNotFoundException {
		log.info("user of username {} is getting retrieved",userName);
		return new ResponseEntity<>(userService.viewUserByUserName(userName), HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/deleteByUserName")
	public ResponseEntity<Void> deleteByUserName(@RequestParam String username) {
		log.info("deleteing user of username {}", username);
		userService.deleteUserByName(username);
		return new ResponseEntity<>(HttpStatus.OK);
	}


}
