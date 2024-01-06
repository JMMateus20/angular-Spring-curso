package com.bolsadeideas.springboot.backend.apirest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.dto.RegistroRegionDTO;
import com.bolsadeideas.springboot.backend.apirest.services.RegionService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/regiones")
public class RegionController {
	
	private final RegionService regionService;
	
	@PostMapping
	public ResponseEntity<?> insertar(@RequestBody RegistroRegionDTO datos){
		return regionService.insertar(datos);
	}
	
	@GetMapping
	public ResponseEntity<?> listar(){
		return regionService.listar();
	}
	
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> listarClientes(@PathVariable Long id){
		return regionService.listarClientes(id);
	}

}
