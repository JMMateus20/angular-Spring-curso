package com.bolsadeideas.springboot.backend.apirest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

	@Getter @Setter
	private String token;
	
}
