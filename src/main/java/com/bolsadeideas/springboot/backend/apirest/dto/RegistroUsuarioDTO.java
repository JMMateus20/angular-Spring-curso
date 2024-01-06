package com.bolsadeideas.springboot.backend.apirest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistroUsuarioDTO {
	
	@NotBlank(message = "no puede estar vacío")
	private String username;
	@NotBlank(message = "no puede estar vacío")
	private String password;

}
