package com.bolsadeideas.springboot.backend.apirest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.services.ProductoService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/productos")
public class ProductoController {
	
	
	private final ProductoService productoService;
	
	@GetMapping("/{termino}")
	public ResponseEntity<?> listarNombres(@PathVariable String termino){
		return this.productoService.listarNombreDeProductos(termino);
		
	}

}
