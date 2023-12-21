package com.authorize.service.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.authorize.exceptions.OrganizationNotFoundException;
import com.authorize.exceptions.SignUpFailedException;
import com.authorize.exceptions.UnAuthorizedException;
import com.authorize.exceptions.UserNotFoundException;
import com.authorize.model.dto.UserDTO;
import com.authorize.model.entity.Organization;
import com.authorize.model.entity.Previlage;
import com.authorize.model.entity.Role;
import com.authorize.model.entity.User;
import com.authorize.repository.OrganizationRepository;
import com.authorize.repository.PrevilageRepository;
import com.authorize.repository.RoleRepository;
import com.authorize.repository.UserRepository;
import com.authorize.service.interfaces.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PrevilageRepository previlageRepository;
	@Autowired
	private OrganizationRepository organizationRepository;

	private Previlage createPrivilege(String name) {
		
		Previlage privilege = previlageRepository.findByName(name);
		if (privilege == null) {
			privilege = new Previlage(name);
			previlageRepository.save(privilege);
			log.info("created previlage {}", name);
		}
		return privilege;
	}

	private void createRole(String name, List<Previlage> privileges) {
		
		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role(name);
			role.setPrevilage(privileges);
			roleRepository.save(role);
			log.info("created role {}", name);
		}
	}

	private void initRolesAndPrevilages() {

		Previlage readPrivilege = createPrivilege("READ_PRIVILEGE");
		Previlage createPrivilege = createPrivilege("CREATE_PRIVILEGE");
		Previlage updatePrivilege = createPrivilege("UPDATE_PRIVILEGE");
		Previlage deletePrivilege = createPrivilege("DELETE_PRIVILEGE");

		createRole("ROLE_PROJECT_ADMIN",
				Arrays.asList(readPrivilege, createPrivilege, updatePrivilege, deletePrivilege));
		createRole("ROLE_FIELD_SUPPORTER", Arrays.asList(createPrivilege, updatePrivilege));
		createRole("ROLE_FIELD_MANAGER", Arrays.asList(readPrivilege));

	}

	@Override
	public User signUpUser(UserDTO newUserDto) throws SignUpFailedException {

		initRolesAndPrevilages();
		List<Role> roles = new ArrayList<>();
		newUserDto.getRole().forEach(role -> {
			roles.add(Optional.ofNullable(roleRepository.findByName(role))
					.orElseThrow(() -> new UnAuthorizedException("invalid role is provided")));
		});

		log.info(roles.toString());
		Organization orgs = Optional.ofNullable(organizationRepository.findByName(newUserDto.getOrgs()))
//				.orElse(organizationRepository.save(Organization.builder().city("Hyd").name(newUserDto.getOrgs()).build()));
				.orElseThrow(() -> new OrganizationNotFoundException("no such org found"));

		User user = User.builder().city(newUserDto.getCity()).role(roles).name(newUserDto.getName())
				.password(passwordEncoder.encode(newUserDto.getPassword())).organization(orgs).build();
		log.info("user is getting created");
		return Optional.ofNullable(user).map(userRepository::save)
				.orElseThrow(() -> new SignUpFailedException("Unable to create user"));
	}

	@Override
	public User update(UserDTO updateUserDto) throws UserNotFoundException {
		List<Role> roles = new ArrayList<>();
		updateUserDto.getRole().forEach(role -> {
			roles.add(Optional.ofNullable(roleRepository.findByName(role))
					.orElseThrow(() -> new UnAuthorizedException("invalid role is provided")));
		});
		log.info("obtained role from db {}", roles.toString());
		Organization orgs = Optional.ofNullable(organizationRepository.findByName(updateUserDto.getOrgs()))
				.orElseThrow(() -> new OrganizationNotFoundException("no such org found"));

		User user = User.builder().city(updateUserDto.getCity()).role(roles).name(updateUserDto.getName())
				.password(updateUserDto.getPassword()).id(updateUserDto.getId()).organization(orgs).build();
		log.info("user is getting updated for score");
		return Optional.ofNullable(user).map(userRepository::save)
				.orElseThrow(() -> new UserNotFoundException("Unable to update user"));
	}

	@Override
	public User viewUserByUserName(String userName) throws UserNotFoundException {
		User user = Optional.ofNullable(userRepository.findByName(userName))
				.orElseThrow(() -> new UserNotFoundException("No user found of username = " + userName));
		log.info("user getting viewd by username {}", userName);
		return user;
	}

	@Override
	public List<User> getUsers() {
		log.info("Fetching all users");
		return userRepository.findAll();
	}

}