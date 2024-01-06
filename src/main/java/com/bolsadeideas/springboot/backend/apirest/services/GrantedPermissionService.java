package com.bolsadeideas.springboot.backend.apirest.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface GrantedPermissionService {
	
	
	ResponseEntity<?> listarPermisosPorRol(Long idRole, Pageable pageable);

}
