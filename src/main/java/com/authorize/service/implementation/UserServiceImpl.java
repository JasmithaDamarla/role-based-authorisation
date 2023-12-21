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
import com.authorize.model.entity.Privilege;
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

	private Privilege createPrivilege(String name) {
		
		Privilege privilege = previlageRepository.findByName(name);
		if (privilege == null) {
			privilege = new Privilege(name);
			previlageRepository.save(privilege);
			log.info("created previlage {}", name);
		}
		return privilege;
	}

	private void createRole(String name, List<Privilege> privileges) {
		
		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role(name);
			role.setPrivilege(privileges);
			roleRepository.save(role);
			log.info("created role {}", name);
		}
	}

	private void initRolesAndPrevilages() {

		Privilege readPrivilege = createPrivilege("READ_PRIVILEGE");
		Privilege createPrivilege = createPrivilege("CREATE_PRIVILEGE");
		Privilege updatePrivilege = createPrivilege("UPDATE_PRIVILEGE");
		Privilege deletePrivilege = createPrivilege("DELETE_PRIVILEGE");

		createRole("ROLE_PROJECT_ADMIN",
				Arrays.asList(readPrivilege, createPrivilege, updatePrivilege, deletePrivilege));
		createRole("ROLE_FIELD_SUPPORTER", Arrays.asList(createPrivilege, updatePrivilege));
		createRole("ROLE_FIELD_MANAGER", Arrays.asList(readPrivilege));

	}

	@Override
	public UserDTO signUpUser(UserDTO newUserDto) throws SignUpFailedException {
	    initRolesAndPrevilages();
	    List<Role> roles = new ArrayList<>();
	    newUserDto.getRole().forEach(role -> {
	        roles.add(Optional.ofNullable(roleRepository.findByName(role))
	                .orElseThrow(() -> new UnAuthorizedException("Invalid role is provided")));
	    });

	    log.info(roles.toString());
	    Organization orgs = Optional.ofNullable(organizationRepository.findByName(newUserDto.getOrgs()))
	            .orElseThrow(() -> new OrganizationNotFoundException("No such org found"));

	    if (userRepository.existsByNameAndOrganizationName(newUserDto.getName(), newUserDto.getOrgs())) {
	        throw new SignUpFailedException("User with the same name already exists in the organization: " + newUserDto.getName());
	    }

	    User reportTo = userRepository.findByName(newUserDto.getReportingTo());
	    User newUser = userRepository.save(User.builder()
	            .city(newUserDto.getCity())
	            .role(roles)
	            .reportingTo(reportTo)
	            .name(newUserDto.getName())
	            .password(passwordEncoder.encode(newUserDto.getPassword()))
	            .organization(orgs)
	            .build());
	    log.info("User is created successfully!!");
	    return convertToDTO(newUser);
	}

	@Override
	public UserDTO update(UserDTO updateUserDto) throws UserNotFoundException {
		List<Role> roles = new ArrayList<>();
		updateUserDto.getRole().forEach(role -> {
			roles.add(Optional.ofNullable(roleRepository.findByName(role))
					.orElseThrow(() -> new UnAuthorizedException("invalid role is provided")));
		});
		log.info("obtained role from db {}", roles.toString());
		Organization orgs = Optional.ofNullable(organizationRepository.findByName(updateUserDto.getOrgs()))
				.orElseThrow(() -> new OrganizationNotFoundException("no such org found"));
		User reportTo = userRepository.findByName(updateUserDto.getReportingTo());
		User updatedUser = userRepository.save(User.builder().city(updateUserDto.getCity()).role(roles).reportingTo(reportTo).name(updateUserDto.getName())
				.password(updateUserDto.getPassword()).id(updateUserDto.getId()).organization(orgs).build());
		
		log.info("User is updated successfully!!");
	    return convertToDTO(updatedUser);
	}

	@Override
	public UserDTO viewUserByUserName(String userName) throws UserNotFoundException {
		User user = Optional.ofNullable(userRepository.findByName(userName))
				.orElseThrow(() -> new UserNotFoundException("No user found of username = " + userName));
		log.info("user getting viewd by username {}", userName);
		return convertToDTO(user);
	}

	@Override
	public List<UserDTO> getUsers() {
		log.info("Fetching all users");
		return userRepository.findAll().stream().map(this::convertToDTO).toList();
	}
	
	@Override
	public void deleteUserByName(String userName) {
		userRepository.deleteByName(userName);
		log.info("user deleted successfully!");
	}

	private UserDTO convertToDTO(User user) {
		log.info("user entity converted into userDTO");
		return UserDTO.builder()
				.id(user.getId())
				.city(user.getCity())
				.name(user.getName())
				.orgs(user.getOrganization().getName())
				.reportingTo(user.getReportingTo().getName())
				.role(user.getRole().stream().map(Role::getName).toList())
				.build();
	}
}