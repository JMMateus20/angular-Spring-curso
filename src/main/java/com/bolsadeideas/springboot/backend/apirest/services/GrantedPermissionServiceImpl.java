package com.bolsadeideas.springboot.backend.apirest.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.backend.apirest.dto.GrantedPermissionDTO;
import com.bolsadeideas.springboot.backend.apirest.entity.Role;
import com.bolsadeideas.springboot.backend.apirest.exceptions.NotFoundExceptionManaged;
import com.bolsadeideas.springboot.backend.apirest.repository.GrantedPermissionRepository;
import com.bolsadeideas.springboot.backend.apirest.repository.RoleRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class GrantedPermissionServiceImpl implements GrantedPermissionService{
	
	
	private final RoleRepository roleRep;
	private final GrantedPermissionRepository gpRep;

	@Override
	public ResponseEntity<?> listarPermisosPorRol(Long idRole, Pageable pageable) {
		Role roleBD=this.roleRep.findById(idRole).orElseThrow(()->new NotFoundExceptionManaged("Rol no encontrado"));
		Page<GrantedPermissionDTO> permisos=this.gpRep.listarPermisosPorRol(roleBD, pageable);
		if (!permisos.hasContent()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(permisos);
	}

}
