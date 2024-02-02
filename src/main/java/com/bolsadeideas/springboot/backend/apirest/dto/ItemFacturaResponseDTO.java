package com.bolsadeideas.springboot.backend.apirest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemFacturaResponseDTO {
	
	private Long id;
	
	
	private Integer cantidad;
	
	private String nomProducto;
	private Long precioProducto;

}
