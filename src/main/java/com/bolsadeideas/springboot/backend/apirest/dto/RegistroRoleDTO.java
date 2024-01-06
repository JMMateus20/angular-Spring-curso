package com.bolsadeideas.springboot.backend.apirest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class RegistroRoleDTO {
	
	
	@Getter @Setter
	@NotBlank(message = "No puede estar vacío")
	private String nombre;

}
