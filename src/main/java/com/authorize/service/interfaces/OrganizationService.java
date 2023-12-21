package com.authorize.service.interfaces;

import java.util.List;

import com.authorize.model.dto.OrganizationDTO;

public interface OrganizationService {

	public OrganizationDTO addOrgs(OrganizationDTO orgs);
	public OrganizationDTO updateOrgs(OrganizationDTO orgs);
	public void deleteOrgs(int id);
	public List<OrganizationDTO> viewOrgs();
	public OrganizationDTO viewOrgsByName(String name);
}
