package com.bolsadeideas.springboot.backend.apirest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroOperacionDTO {
	
	@NotBlank(message = "no puede estar vacío")
	private String nombre;
	
	private String path;
	@NotBlank(message = "no puede estar vacío")
	private String metodo_http;
	@NotNull(message = "no puede estar vacío")
	private boolean publico;
	

}
