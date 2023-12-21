package com.authorize.service.interfaces;

import java.util.List;

import com.authorize.model.dto.FieldDTO;
import com.authorize.model.entity.Field;

public interface FieldService {
	
	public Field addField(FieldDTO field);
	public Field updateField(FieldDTO field);
	public void deleteField(int id);
	public List<Field> viewFields();
}
