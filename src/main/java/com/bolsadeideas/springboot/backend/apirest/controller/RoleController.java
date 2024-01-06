package com.bolsadeideas.springboot.backend.apirest.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.dto.RegistroRoleDTO;
import com.bolsadeideas.springboot.backend.apirest.entity.Usuario;
import com.bolsadeideas.springboot.backend.apirest.services.RoleService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/roles")
public class RoleController {
	
	private final RoleService roleService;
	
	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody RegistroRoleDTO datos, BindingResult resultado){
		if (resultado.hasErrors()) {
			List<String> errores=resultado.getFieldErrors().stream().map(err->"el campo: '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
			return ResponseEntity.badRequest().body(errores);
		}
		return roleService.insertar(datos);
	}
	
	@GetMapping("/usuarios/{idRole}")
	public ResponseEntity<List<Usuario>> verUsuarios(@PathVariable Long idRole){
		return roleService.verUsuarios(idRole);
	}
	
	@PostMapping("/operaciones")
	public ResponseEntity<?> agregarOperacion(@RequestParam(name="rol") Long idRole,@RequestParam(name="operacion") Long idOperacion){
		return this.roleService.agregarOperacion(idRole, idOperacion);
	}

}
