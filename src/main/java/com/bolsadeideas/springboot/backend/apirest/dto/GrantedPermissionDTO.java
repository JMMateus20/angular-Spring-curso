package com.bolsadeideas.springboot.backend.apirest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrantedPermissionDTO {
	
	private Long idPermiso;
	
	private Long operacionId;
	
	private String nombreOperacion;
	private String path;
	private String metodo_http;
	private boolean publico;
	private String nombreModulo;

}
