package com.authorize.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.authorize.model.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer>{
	Organization findByName(String name);
}
