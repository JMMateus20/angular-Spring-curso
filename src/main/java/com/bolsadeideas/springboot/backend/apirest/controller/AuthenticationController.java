package com.bolsadeideas.springboot.backend.apirest.controller;

import java.util.Map;

import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.dto.LoginRequestDTO;
import com.bolsadeideas.springboot.backend.apirest.dto.LoginResponseDTO;
import com.bolsadeideas.springboot.backend.apirest.dto.LogoutResponse;
import com.bolsadeideas.springboot.backend.apirest.services.auth.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationService authService;
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO credenciales){
		return ResponseEntity.ok(authService.login(credenciales));
		
	}
	
	@GetMapping
	public ResponseEntity<Boolean> validarToken(@RequestParam String jwt){
		boolean isTokenValid=this.authService.validateToken(jwt);
		return ResponseEntity.ok(isTokenValid);
	}
	
	@GetMapping("/profile")
	public ResponseEntity<Map<String, Object>> verPerfil(){
		return ResponseEntity.ok(authService.encontrarUsuarioLogeado());
	}
	
	
	@PostMapping("/logout")
	public ResponseEntity<LogoutResponse> logout(HttpServletRequest request){
		authService.logout(request);
		return ResponseEntity.ok(new LogoutResponse("Logout exitoso"));
	}
	

}
