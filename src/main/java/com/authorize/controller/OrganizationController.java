package com.authorize.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.authorize.model.dto.OrganizationDTO;
import com.authorize.service.interfaces.OrganizationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/orgs")
@RestController
public class OrganizationController {
	
	@Autowired
	private OrganizationService organizationService;
	
	@PostMapping
	public ResponseEntity<OrganizationDTO> createOrganization(@RequestBody OrganizationDTO orgs) {
		log.info("orgs data getting created");
		return new ResponseEntity<>(organizationService.addOrgs(orgs),HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAuthority('READ_PRIVILEGE')")
	@GetMapping("/orgsName")
	public ResponseEntity<OrganizationDTO> readOrganizationByName(@RequestParam String name) {
		log.info("obtaining org data from service");
		return new ResponseEntity<>(organizationService.viewOrgsByName(name),HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<OrganizationDTO>> readOrganization() {
		log.info("obtaining data from service");
		return new ResponseEntity<>(organizationService.viewOrgs(),HttpStatus.OK);
	}
	
//	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_SUPPORTER')")
	@PutMapping
	public ResponseEntity<OrganizationDTO> updateOrganization(@RequestBody OrganizationDTO orgs) {
		log.info("orgs data getting updated");
		return new ResponseEntity<>(organizationService.updateOrgs(orgs),HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrganization(@PathVariable int id) {
		organizationService.deleteOrgs(id);
		log.info("deleted org successfully");
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
