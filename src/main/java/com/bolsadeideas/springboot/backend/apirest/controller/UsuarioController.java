package com.bolsadeideas.springboot.backend.apirest.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.dto.RegistroUsuarioDTO;
import com.bolsadeideas.springboot.backend.apirest.entity.Role;
import com.bolsadeideas.springboot.backend.apirest.services.UsuarioService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {
	
	
	private final UsuarioService usuarioService;
	
	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody RegistroUsuarioDTO datos, BindingResult resultado){
		if (resultado.hasErrors()) {
			List<String> errores=resultado.getFieldErrors().stream().map(err->"el campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
			return ResponseEntity.badRequest().body(errores);
		}
		return usuarioService.insertar(datos);
	}
	
	@PostMapping("/roles")
	public ResponseEntity<?> agregarRol(@RequestParam(name = "usuario", required = true) Long idUsuario,@RequestParam(name = "rol", required = true) Long idRol){
		return usuarioService.agregarRol(idUsuario, idRol);
	}
	
	@GetMapping("/roles/{idUsuario}")
	public ResponseEntity<List<Role>> verRoles(@PathVariable Long idUsuario){
		return usuarioService.verRoles(idUsuario);
	}
	
	@DeleteMapping("/roles")
	public ResponseEntity<?> eliminarRol(@RequestParam(name = "usuario", required = true) Long idUsuario,@RequestParam(name = "rol", required = true) Long idRol){
		return usuarioService.eliminarRol(idUsuario, idRol);
	}

}
