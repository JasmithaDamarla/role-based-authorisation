package com.authorize.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.authorize.model.entity.Field;

@Repository
public interface FieldRepository extends JpaRepository<Field, Integer>{
	Field findByTitle(String title);
}
