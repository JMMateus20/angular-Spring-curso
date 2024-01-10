package com.bolsadeideas.springboot.backend.apirest.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessDeniedExceptionDTO {
	
	
	private String mensajeBackend;
	private String mensaje;
	
	private String url;
	private String method;
	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime fecha;

}
