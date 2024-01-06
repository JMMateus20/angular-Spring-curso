package com.bolsadeideas.springboot.backend.apirest.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.dto.RegistroModuloDTO;
import com.bolsadeideas.springboot.backend.apirest.dto.RegistroOperacionDTO;
import com.bolsadeideas.springboot.backend.apirest.services.ModuloService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
@AllArgsConstructor
@RestController
@RequestMapping("/modulos")
public class ModuloController {
	
	
	private final ModuloService moduloService;
	
	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody RegistroModuloDTO datos, BindingResult resultado){
		if (resultado.hasErrors()) {
			return this.validationErrors(resultado);
		}
		return this.moduloService.insertar(datos);
	}
	
	@PostMapping("/operaciones/{moduloId}")
	public ResponseEntity<?> agregarOperacion(@PathVariable Long moduloId,@Valid @RequestBody RegistroOperacionDTO datos, BindingResult resultado){
		if (resultado.hasErrors()) {
			return this.validationErrors(resultado);
		}
		return this.moduloService.agregarOperacion(moduloId, datos);
	}
	
	@DeleteMapping("/operaciones")
	public ResponseEntity<?> eliminarOperacion(@RequestParam(name = "modulo") Long moduloId,@RequestParam(name = "operacion") Long operacionId){
		return this.moduloService.eliminarOperacion(moduloId, operacionId);
	}
	
	@GetMapping("/operaciones/{moduloId}")
	public ResponseEntity<?> listarOperaciones(@PathVariable Long moduloId,@RequestParam(name = "page") Integer page){
		return this.moduloService.listarOperaciones(moduloId, PageRequest.of(page, 2));
	}
	
	@GetMapping("/operaciones2/{moduloId}")
	public ResponseEntity<?> listarOperaciones2(@PathVariable Long moduloId){
		return this.moduloService.listarOperaciones2(moduloId);
	}
	
	@PutMapping("/operaciones")
	public ResponseEntity<?> actualizarOperacion(@RequestParam(name="modulo") Long moduloId,@RequestParam(name="operacion") Long operacionId,@Valid @RequestBody RegistroOperacionDTO datos, BindingResult resultado){
		if (resultado.hasErrors()) {
			return this.validationErrors(resultado);
		}
		return this.moduloService.actualizarOperacion(moduloId, operacionId, datos);
	}
	
	@PutMapping("/{moduloId}")
	public ResponseEntity<?> actualizarModulo(@PathVariable Long moduloId, @Valid @RequestBody RegistroModuloDTO datos, BindingResult resultado){
		if (resultado.hasErrors()) {
			return this.validationErrors(resultado);
		}
		return this.moduloService.modificarModulo(moduloId, datos);
	}
	
	private ResponseEntity<List<String>> validationErrors(BindingResult resultado){
		List<String> errores=resultado.getFieldErrors().stream().map(err->"El campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
		return ResponseEntity.badRequest().body(errores);
	}
	
	
	

}
