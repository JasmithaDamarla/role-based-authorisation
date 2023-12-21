package com.authorize.service.interfaces;

import java.util.List;

import com.authorize.model.dto.FieldDTO;

public interface FieldService {
	
	public FieldDTO addField(FieldDTO field);
	public FieldDTO updateField(FieldDTO field);
	public void deleteField(int id);
	public List<FieldDTO> viewFields();
}
