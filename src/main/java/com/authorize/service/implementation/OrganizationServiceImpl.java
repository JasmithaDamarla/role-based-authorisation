package com.authorize.service.implementation;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.authorize.exceptions.FieldNotFoundException;
import com.authorize.exceptions.OrganizationNotFoundException;
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
	public OrganizationDTO addOrgs(OrganizationDTO orgs) {
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
		Organization newOrgs = organizationRepository.save(
				Organization.builder().city(orgs.getCity()).fields(fields).users(users).name(orgs.getName()).build());
		log.info("created orgs data succesfully");
		return convertToDTO(newOrgs);
	}

	@Override
	public OrganizationDTO updateOrgs(OrganizationDTO orgs) {
		Set<Field> fields = new HashSet<>();
		orgs.getFields().forEach(field -> {
			Field curField = Optional.ofNullable(fieldRepository.findByTitle(field))
					.orElseThrow(() -> new FieldNotFoundException("no such field " + field + " found"));
			fields.add(curField);
		});
		Set<User> users = new HashSet<>();
		orgs.getUsers().forEach(user -> {
			User existingUser = Optional.ofNullable(userRepository.findByName(user))
					.orElseThrow(() -> new UserNotFoundException("no such such " + user + " found"));
			users.add(existingUser);
			existingUser.setOrganization(Organization.builder().id(orgs.getId()).city(orgs.getCity()).fields(fields)
					.users(users).name(orgs.getName()).build());
			userRepository.save(existingUser);
		});
		log.info("users and field are retrieved");
		Organization updateOrgs = organizationRepository.save(Organization.builder().id(orgs.getId())
				.city(orgs.getCity()).fields(fields).users(users).name(orgs.getName()).build());
		log.info("updated orgs data succesfully");
		return convertToDTO(updateOrgs);
	}

	@Override
	public void deleteOrgs(int id) {
		log.info("deleting org of id {}", id);
		organizationRepository.deleteById(id);
	}

	@Transactional
	@Override
	public List<OrganizationDTO> viewOrgs() {
		log.info("loading all rows");
		return organizationRepository.findAll().stream().map(this::convertToDTO).toList();
	}

	@Override
	public OrganizationDTO viewOrgsByName(String name) {
		Organization orgs = Optional.ofNullable(organizationRepository.findByName(name))
				.orElseThrow(() -> new OrganizationNotFoundException("orgs not found of name " + name));
		log.info("obtained org of name {}", name);
		return convertToDTO(orgs);
	}

	private OrganizationDTO convertToDTO(Organization orgs) {
		return OrganizationDTO.builder().city(orgs.getCity()).name(orgs.getName()).id(orgs.getId())
				.fields(orgs.getFields().stream().map(Field::getTitle).collect(Collectors.toSet()))
				.users(orgs.getUsers().stream().map(User::getName).collect(Collectors.toSet())).build();
	}

}
