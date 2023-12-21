package com.authorize.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.authorize.model.entity.Privilege;

@Repository
public interface PrevilageRepository extends JpaRepository<Privilege, Integer>{

	Privilege findByName(String name);
}
