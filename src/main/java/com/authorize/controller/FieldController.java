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
import com.authorize.model.entity.Field;
import com.authorize.service.interfaces.FieldService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/fields")
@RestController
public class FieldController {
	
	@Autowired
	private FieldService fieldService;
	
	@PostMapping
	public ResponseEntity<String> createField(@RequestBody FieldDTO field) {
		log.info("field is getting created {}",field.toString());
		fieldService.addField(field);
		return new ResponseEntity<>("Field created suucessfully!",HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<Field>> readFields() {
		log.info("obtaining data from service");
		return new ResponseEntity<>(fieldService.viewFields(),HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<String> updateField(@RequestBody FieldDTO field) {
		log.info("updating data {}",field);
		fieldService.updateField(field);
		return new ResponseEntity<>("Field updated suucessfully!",HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteField(@PathVariable int id) {
		fieldService.deleteField(id);
		log.info("deleted field successfully");
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
