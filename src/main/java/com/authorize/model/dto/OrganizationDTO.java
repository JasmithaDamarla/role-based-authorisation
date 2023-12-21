package com.authorize.model.dto;

import java.util.Set;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO {

	private Integer id;
	@Size(min = 3, max = 8, message = "min length of 3 and max of 8")
	private String name;
	private String city;
	private Set<String> users;
	private Set<String> fields;
}
