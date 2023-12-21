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
import org.springframework.web.bind.annotation.RestController;

import com.authorize.model.dto.OrganizationDTO;
import com.authorize.model.entity.Organization;
import com.authorize.service.interfaces.OrganizationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/orgs")
@RestController
public class OrganizationController {
	
	@Autowired
	private OrganizationService organizationService;
	
//	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<String> createOrganization(@RequestBody OrganizationDTO orgs) {
		organizationService.addOrgs(orgs);
		log.info("created orgs succesfully");
		return new ResponseEntity<>("Orgs created successfullyy!!",HttpStatus.CREATED);
	}
	
//	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPPORTER') or hasAuthority('ROLE_MANAGER')")
	@GetMapping
	public ResponseEntity<List<Organization>> readOrganization() {
		log.info("obtaining data from service");
		return new ResponseEntity<>(organizationService.viewOrgs(),HttpStatus.OK);
	}
	
//	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPPORTER')")
	@PutMapping
	public ResponseEntity<String> updateOrganization(@RequestBody OrganizationDTO orgs) {
		organizationService.updateOrgs(orgs);
		log.info("updated orgs data succesfully");
		return new ResponseEntity<>("updated orgs successfullyy",HttpStatus.ACCEPTED);
	}

//	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPPORTER')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrganization(@PathVariable int id) {
		organizationService.deleteOrgs(id);
		log.info("deleted org successfully");
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
