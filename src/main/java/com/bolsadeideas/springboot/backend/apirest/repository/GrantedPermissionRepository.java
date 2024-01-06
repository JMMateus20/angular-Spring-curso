package com.bolsadeideas.springboot.backend.apirest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bolsadeideas.springboot.backend.apirest.dto.GrantedPermissionDTO;
import com.bolsadeideas.springboot.backend.apirest.entity.GrantedPermission;
import com.bolsadeideas.springboot.backend.apirest.entity.Role;

public interface GrantedPermissionRepository extends JpaRepository<GrantedPermission, Long>{
	
	@Query("SELECT new com.bolsadeideas.springboot.backend.apirest.dto.GrantedPermissionDTO(G.id, G.operacion.id, G.operacion.nombre, G.operacion.path, G.operacion.metodo_http, G.operacion.publico, G.operacion.modulo.nombre) FROM GrantedPermission AS G WHERE G.role=:role")
	Page<GrantedPermissionDTO> listarPermisosPorRol(@Param(value="role") Role role, Pageable pageable);  

}
