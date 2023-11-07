package com.bolsadeideas.springboot.backend.apirest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.dto.RegistroClienteDTO;
import com.bolsadeideas.springboot.backend.apirest.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.services.ClienteService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@CrossOrigin(origins = {"http://localhost:4200"})
@AllArgsConstructor
@RestController
@RequestMapping(value = "/clientes")
public class ClienteController {
	
	
	private final ClienteService clienteService;
	
	@PostMapping
	public ResponseEntity<?> insertar(@Valid @RequestBody RegistroClienteDTO datos, BindingResult resultado){
		if (resultado.hasErrors()) {
			List<String> errores=resultado.getFieldErrors().stream().map(err->"el campo: '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
			return ResponseEntity.badRequest().body(errores);
		}
		return clienteService.insertar(datos);
	}
	
	@GetMapping()
	public List<Cliente> listar(){
		return clienteService.listar();
	}
	
	@GetMapping("/page/{page}")
	public Page<Cliente> listarPage(@PathVariable Integer page){
		return clienteService.listar(PageRequest.of(page, 5, Sort.by("createAt").descending()));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> encontrar(@PathVariable Long id){
		return clienteService.encontrar(id);
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		return clienteService.eliminar(id);
	}
	

	@PutMapping("/{id}")
	public ResponseEntity<?> modificar(@PathVariable Long id,@Valid @RequestBody RegistroClienteDTO datos, BindingResult resultado){
		if (resultado.hasErrors()) {
			List<String> errores=resultado.getFieldErrors().stream().map(err->{
				return "el campo: '" + err.getField() + "' " + err.getDefaultMessage();
			}).collect(Collectors.toList());
			return ResponseEntity.badRequest().body(errores);
		}
		return clienteService.modificar(id, datos);
	}
	
	
	

}