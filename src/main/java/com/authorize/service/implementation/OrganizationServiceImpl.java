package com.authorize.service.implementation;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.authorize.exceptions.FieldNotFoundException;
import com.authorize.exceptions.UserNotFoundException;
import com.authorize.model.dto.OrganizationDTO;
import com.authorize.model.entity.Field;
import com.authorize.model.entity.Organization;
import com.authorize.model.entity.User;
import com.authorize.repository.FieldRepository;
import com.authorize.repository.OrganizationRepository;
import com.authorize.repository.UserRepository;
import com.authorize.service.interfaces.OrganizationService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrganizationServiceImpl implements OrganizationService {

	@Autowired
	private OrganizationRepository organizationRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FieldRepository fieldRepository;

	@Override
	public Organization addOrgs(OrganizationDTO orgs) {
		log.info("entered service of inserting..{}", orgs.toString());
		Set<Field> fields = new HashSet<>();
		orgs.getFields().forEach(field -> {
			fields.add(Optional.ofNullable(fieldRepository.findByTitle(field))
					.orElseThrow(() -> new FieldNotFoundException("no such field " + field + " found in fields")));
		});
		Set<User> users = new HashSet<>();
		orgs.getUsers().forEach(user -> {
			users.add(Optional.ofNullable(userRepository.findByName(user))
					.orElseThrow(() -> new UserNotFoundException("no such user " + user + " found in db")));
		});
		log.info("fields and users obtained");
		return organizationRepository.save(
				Organization.builder().city(orgs.getCity()).fields(fields).users(users).name(orgs.getName()).build());
	}

	@Override
	public Organization updateOrgs(OrganizationDTO orgs) {
		log.info("entered service of updating..");
		Set<Field> fields = new HashSet<>();
		orgs.getFields().forEach(field -> {
			Field curField = Optional.ofNullable(fieldRepository.findByTitle(field))
					.orElseThrow(() -> new FieldNotFoundException("no such field " + field + " found"));
			fields.add(curField);
		});
		Set<User> users = new HashSet<>();
		orgs.getUsers().forEach(user -> {
//			users.add(Optional.ofNullable(userRepository.findByName(user))
//					.orElseThrow(() -> new UserNotFoundException("no such such " + user + " found")));
			User existingUser = Optional.ofNullable(userRepository.findByName(user))
					.orElseThrow(() -> new UserNotFoundException("no such such " + user + " found"));
			users.add(existingUser);
			existingUser.setOrganization(Organization.builder().id(orgs.getId()).city(orgs.getCity()).fields(fields)
					.users(users).name(orgs.getName()).build());
			userRepository.save(existingUser);
		});
		log.info("users and field are retrieved");
		return organizationRepository.save(Organization.builder().id(orgs.getId()).city(orgs.getCity()).fields(fields)
				.users(users).name(orgs.getName()).build());
	}

	@Override
	public void deleteOrgs(int id) {
		log.info("deleting org of id {}", id);
		organizationRepository.deleteById(id);
	}

	@Transactional
	@Override
	public List<Organization> viewOrgs() {
		log.info("loading all rows");
		return organizationRepository.findAll();
	}

}
