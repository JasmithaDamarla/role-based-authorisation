package com.authorize.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.authorize.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
	User findByName(String name);
	boolean existsByNameAndOrganizationName(String reportingTo, String orgs);
	void deleteByName(String userName);
	
}
