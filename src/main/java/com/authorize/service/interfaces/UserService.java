package com.authorize.service.interfaces;

import java.util.List;

import com.authorize.exceptions.SignUpFailedException;
import com.authorize.exceptions.UserNotFoundException;
import com.authorize.model.dto.UserDTO;

public interface UserService {
	
	UserDTO signUpUser(UserDTO user) throws SignUpFailedException;
	UserDTO update(UserDTO updateUser) throws UserNotFoundException;
	List<UserDTO> getUsers();
	UserDTO viewUserByUserName(String userName) throws UserNotFoundException;
	void deleteUserByName(String userName);
}
