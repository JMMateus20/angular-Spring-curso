package com.bolsadeideas.springboot.backend.apirest.services.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.backend.apirest.dto.LoginRequestDTO;
import com.bolsadeideas.springboot.backend.apirest.dto.LoginResponseDTO;
import com.bolsadeideas.springboot.backend.apirest.dto.UsuarioResponseDTO;
import com.bolsadeideas.springboot.backend.apirest.entity.Usuario;
import com.bolsadeideas.springboot.backend.apirest.exceptions.AuthenticationExceptionManaged;
import com.bolsadeideas.springboot.backend.apirest.repository.UsuarioRepository;

@Service
public class AuthenticationService {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private UsuarioRepository usuarioRep;
	
	
	public LoginResponseDTO login(LoginRequestDTO credenciales) {
		UsernamePasswordAuthenticationToken upat=new UsernamePasswordAuthenticationToken(credenciales.getUsername(), credenciales.getContrasena());
		try {
			authManager.authenticate(upat);
		}catch(AuthenticationException e) {
			throw new AuthenticationExceptionManaged("Username o contraseña incorrectas");
		}
		Usuario usuario=this.usuarioRep.findByUsername(credenciales.getUsername()).get();
		return new LoginResponseDTO(this.jwtService.generarToken(usuario, generarClaims(usuario)));
	}
	
	private Map<String, Object> generarClaims(Usuario usuario){
		Map<String, Object> extraClaims=new HashMap<>();
		List<String> roles=usuario.getRoles().stream().map(rol->rol.getNombre()).collect(Collectors.toList());
		extraClaims.put("roles", roles);
		return extraClaims;
	}
	
	
	public boolean validateToken(String jwt) {
		try {
			jwtService.extractUsername(jwt);
			return true;
		}catch(Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	
	public Map<String, Object> encontrarUsuarioLogeado() {
		Map<String, Object> respuesta=new HashMap<>();
		String username=(String) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Usuario usuarioBD=this.usuarioRep.findByUsername(username).get();
		UsuarioResponseDTO dto=new UsuarioResponseDTO(usuarioBD.getUsername(), 
				usuarioBD.getRoles().stream().map(rol->rol.getNombre()).collect(Collectors.toList()));
		respuesta.put("usuario", dto);
		return respuesta;
	}

}
