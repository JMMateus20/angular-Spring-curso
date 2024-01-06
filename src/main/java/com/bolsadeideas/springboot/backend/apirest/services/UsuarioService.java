package com.bolsadeideas.springboot.backend.apirest.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.bolsadeideas.springboot.backend.apirest.dto.RegistroUsuarioDTO;
import com.bolsadeideas.springboot.backend.apirest.entity.Role;

public interface UsuarioService{

	ResponseEntity<?> insertar(RegistroUsuarioDTO datos);
	
	
	ResponseEntity<?> agregarRol(Long idUsuario, Long idRol);
	
	
	ResponseEntity<List<Role>> verRoles(Long idUsuario);
	
	
	ResponseEntity<?> eliminarRol(Long idUsuario, Long idRol);
}
