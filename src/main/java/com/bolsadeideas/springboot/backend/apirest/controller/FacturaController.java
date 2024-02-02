package com.bolsadeideas.springboot.backend.apirest.controller;

import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
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

import com.bolsadeideas.springboot.backend.apirest.dto.RegistroItemFacturaDTO;
import com.bolsadeideas.springboot.backend.apirest.services.FacturaService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/facturas")
public class FacturaController {
	
	
	private final FacturaService facturaService;
	
	
	@GetMapping("/{clienteId}")
	public ResponseEntity<?> listarPorCliente(@PathVariable Long clienteId,@RequestParam(required = true) Integer page){
		return this.facturaService.listarPorCliente(clienteId, PageRequest.of(page, 2));
	}
	
	@PostMapping("/items/{idFactura}")
	public ResponseEntity<?> agregarItem(@PathVariable Long idFactura,@Valid @RequestBody RegistroItemFacturaDTO datos, BindingResult resultado){
		if (resultado.hasErrors()) {
			return ResponseEntity.badRequest().body(resultado.getFieldErrors().stream().map(err->"El campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList()));
		}
		return this.facturaService.agregarItem(idFactura, datos);
	}
	
	@GetMapping("/items/{idFactura}")
	public ResponseEntity<?> listarItems(@PathVariable Long idFactura,@RequestParam(required = true) Integer page){
		return this.facturaService.listarItems(idFactura, PageRequest.of(page, 2));
	}
	
	@DeleteMapping("/items")
	public ResponseEntity<?> eliminarItem(@RequestParam(name = "factura", required = true) Long idFactura, @RequestParam(name = "item", required = true) Long idItem){
		return this.facturaService.eliminarItem(idFactura, idItem);
	}
	
	@GetMapping("/items")
	public ResponseEntity<?> encontrarItem(@RequestParam(name = "factura", required = true) Long idFactura, @RequestParam(name = "item", required = true) Long idItem){
		return this.facturaService.encontrarItem(idFactura, idItem);
	}

}
