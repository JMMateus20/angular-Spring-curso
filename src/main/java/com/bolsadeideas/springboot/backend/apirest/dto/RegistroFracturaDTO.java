package com.bolsadeideas.springboot.backend.apirest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroFracturaDTO {
	
	@NotBlank(message="no puede estar vacío")
	private String descripcion;

	private String observacion;
	

}
