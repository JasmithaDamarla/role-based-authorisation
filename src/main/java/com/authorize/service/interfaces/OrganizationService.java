package com.authorize.service.interfaces;

import java.util.List;

import com.authorize.model.dto.OrganizationDTO;
import com.authorize.model.entity.Organization;

public interface OrganizationService {

	public Organization addOrgs(OrganizationDTO orgs);
	public Organization updateOrgs(OrganizationDTO orgs);
	public void deleteOrgs(int id);
	public List<Organization> viewOrgs();
}
