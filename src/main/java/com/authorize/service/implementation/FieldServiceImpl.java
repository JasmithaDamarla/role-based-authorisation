package com.authorize.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.authorize.exceptions.OrganizationNotFoundException;
import com.authorize.model.dto.FieldDTO;
import com.authorize.model.entity.Field;
import com.authorize.model.entity.Organization;
import com.authorize.repository.FieldRepository;
import com.authorize.repository.OrganizationRepository;
import com.authorize.service.interfaces.FieldService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FieldServiceImpl implements FieldService {

	@Autowired
	private FieldRepository fieldRepository;
	@Autowired
	private OrganizationRepository organizationRepository;

	@Override
	public FieldDTO addField(FieldDTO field) {
		Organization orgs = Optional.ofNullable(organizationRepository.findByName(field.getFarmHolding()))
				.orElseThrow(() -> new OrganizationNotFoundException("no org found with provided name"));
		Field newField = fieldRepository.save(Field.builder().title(field.getTitle()).farmHolding(orgs).build());
		log.info("new field created successfully!");
		return convertToDTO(newField);
	}

	@Override
	public FieldDTO updateField(FieldDTO field) {
		log.info("entered service of updating..{}", field.toString());
		Organization orgs = Optional.ofNullable(organizationRepository.findByName(field.getFarmHolding()))
				.orElseThrow(() -> new OrganizationNotFoundException("no org found with provided name"));
		Field updatedField = fieldRepository
				.save(Field.builder().id(field.getId()).title(field.getTitle()).farmHolding(orgs).build());
		log.info("updated field successfully!!");
		return convertToDTO(updatedField);
	}

	@Override
	public void deleteField(int id) {
		fieldRepository.deleteById(id);
		log.info("deleted field successfully");
	}

	@Override
	public List<FieldDTO> viewFields() {
		log.info("obtaining data from db");
		return fieldRepository.findAll().stream().map(this::convertToDTO).toList();
	}

	private FieldDTO convertToDTO(Field field) {
		return FieldDTO.builder().id(field.getId()).title(field.getTitle()).farmHolding(field.getTitle()).build();
	}
}
