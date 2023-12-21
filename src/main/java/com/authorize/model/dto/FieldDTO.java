package com.authorize.model.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FieldDTO {

	private Integer id;
	@Size(min = 3, max = 8, message = "min length of 3 and max of 8")
	private String title;
	private String farmHolding;
}
