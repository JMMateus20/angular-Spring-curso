package com.bolsadeideas.springboot.backend.apirest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoResponseDTO {
	
	
	private Long id;
	private String nombre;

}
