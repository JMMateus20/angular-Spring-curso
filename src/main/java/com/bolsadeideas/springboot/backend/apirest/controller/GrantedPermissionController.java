package com.bolsadeideas.springboot.backend.apirest.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.services.GrantedPermissionService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/permisos")
public class GrantedPermissionController {
	
	
	private final GrantedPermissionService gpService;
	
	@GetMapping("/{idRole}")
	public ResponseEntity<?> listarPermisosPorRol(@PathVariable Long idRole, @RequestParam(name="page") Integer page){
		return this.gpService.listarPermisosPorRol(idRole, PageRequest.of(page, 2));
	}

}
