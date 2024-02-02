package com.bolsadeideas.springboot.backend.apirest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegistroItemFacturaDTO {
	
	@NotNull(message = "no puede estar vacío")
	@Min(value = 1, message = "la cantidad debe ser al menos 1")
	private Integer cantidad;
	
	@NotNull(message = "no puede estar vacío")
	private Long productoId;

}
