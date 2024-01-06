package com.bolsadeideas.springboot.backend.apirest.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.bolsadeideas.springboot.backend.apirest.dto.RegistroRoleDTO;
import com.bolsadeideas.springboot.backend.apirest.entity.Usuario;

public interface RoleService {
	
	ResponseEntity<?> insertar(RegistroRoleDTO datos);
	
	ResponseEntity<List<Usuario>> verUsuarios(Long idRole);
	
	
	ResponseEntity<?> agregarOperacion(Long idRole, Long idOperacion);

}
