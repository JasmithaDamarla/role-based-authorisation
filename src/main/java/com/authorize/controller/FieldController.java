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

import com.authorize.model.dto.FieldDTO;
import com.authorize.service.interfaces.FieldService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/fields")
@RestController
public class FieldController {
	
	@Autowired
	private FieldService fieldService;
	
	@PostMapping
	public ResponseEntity<FieldDTO> createField(@RequestBody FieldDTO field) {
		log.info("field is getting created {}",field.toString());
		return new ResponseEntity<>(fieldService.addField(field),HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<FieldDTO>> readFields() {
		log.info("obtaining data from service");
		return new ResponseEntity<>(fieldService.viewFields(),HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<FieldDTO> updateField(@RequestBody FieldDTO field) {
		log.info("updating data {}",field);
		return new ResponseEntity<>(fieldService.updateField(field),HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteField(@PathVariable int id) {
		log.info("deleteing field of id {}", id);
		fieldService.deleteField(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
