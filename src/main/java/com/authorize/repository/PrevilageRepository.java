package com.authorize.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.authorize.model.entity.Previlage;

@Repository
public interface PrevilageRepository extends JpaRepository<Previlage, Integer>{

	Previlage findByName(String name);
}
