package com.authorize.model.dto;

import java.util.List;

import jakarta.validation.constraints.Pattern;
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
public class UserDTO {

	private Integer id;
	@Size(min = 3, max = 8, message = "min length of 3 and max of 8")
	private String name;
	@Size(min = 4, message = "min 4 characters")
	private String password;
	private String city;
	@Pattern(regexp = "^(ROLE_PROJECT_ADMIN|ROLE_FILED_SUPPORTER|ROLE_FIELD_MANAGER)$", message = "role must be either ROLE_PROJECT_ADMIN, ROLE_FIELD_SUPPORTER or ROLE_FIELD_MANAGER")
	private List<String> role;
	private String orgs;
	private String reportingTo;
}