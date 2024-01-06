package com.bolsadeideas.springboot.backend.apirest.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.dto.RegistroOperacionDTO;
import com.bolsadeideas.springboot.backend.apirest.services.OperacionService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/operaciones")
public class OperacionController {
	
	private final OperacionService operacionService;
	
	
	@DeleteMapping("/{operacionId}")
	public ResponseEntity<String> eliminar(@PathVariable Long operacionId){
		return this.operacionService.eliminar(operacionId);
	}
	
	@PutMapping("/{operacionId}")
	public ResponseEntity<?> actualizar(@PathVariable Long operacionId,@Valid @RequestBody RegistroOperacionDTO datos, BindingResult resultado){
		if (resultado.hasErrors()) {
			return this.validationErrors(resultado);
		}
		return this.operacionService.actualizar(operacionId, datos);
	}
	
	
	private ResponseEntity<List<String>> validationErrors(BindingResult resultado){
		List<String> errores=resultado.getFieldErrors().stream().map(err->"El campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
		return ResponseEntity.badRequest().body(errores);
	}

}
