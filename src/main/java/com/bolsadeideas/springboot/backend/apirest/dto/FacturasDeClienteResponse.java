package com.bolsadeideas.springboot.backend.apirest.dto;


import java.util.Date;





import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacturasDeClienteResponse {
	
	private Long id;
	
	private String descripcion;
	
	private String observacion;

	private Date createAt;

}
