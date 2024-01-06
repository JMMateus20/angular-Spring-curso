package com.bolsadeideas.springboot.backend.apirest.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {
	
	
	private String username;
	private List<String> roles=new ArrayList<>();

}
