package com.bolsadeideas.springboot.backend.apirest.services.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bolsadeideas.springboot.backend.apirest.dto.LoginRequestDTO;
import com.bolsadeideas.springboot.backend.apirest.dto.LoginResponseDTO;
import com.bolsadeideas.springboot.backend.apirest.dto.UsuarioResponseDTO;
import com.bolsadeideas.springboot.backend.apirest.entity.JwtToken;
import com.bolsadeideas.springboot.backend.apirest.entity.Usuario;
import com.bolsadeideas.springboot.backend.apirest.exceptions.AuthenticationExceptionManaged;
import com.bolsadeideas.springboot.backend.apirest.repository.JwtTokenRepository;
import com.bolsadeideas.springboot.backend.apirest.repository.UsuarioRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthenticationService {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private UsuarioRepository usuarioRep;
	
	@Autowired
	private JwtTokenRepository jwtTokenRep;
	
	
	public LoginResponseDTO login(LoginRequestDTO credenciales) {
		UsernamePasswordAuthenticationToken upat=new UsernamePasswordAuthenticationToken(credenciales.getUsername(), credenciales.getContrasena());
		
		authManager.authenticate(upat);
		
			
		Usuario usuario=this.usuarioRep.findByUsername(credenciales.getUsername()).get();
		String jwt=this.jwtService.generarToken(usuario, generarClaims(usuario));
		this.jwtTokenRep.save(new JwtToken(jwt, jwtService.extractExpiration(jwt), true, usuario));
		return new LoginResponseDTO(jwt);
	}
	
	private Map<String, Object> generarClaims(Usuario usuario){
		Map<String, Object> extraClaims=new HashMap<>();
		List<String> permisos=usuario.getAuthorities().stream().map(per->per.getAuthority()).collect(Collectors.toList()); //se mapean los authorities del usuario a tipo string para que en angular se puedan leer
		extraClaims.put("permisos", permisos);
		extraClaims.put("id", usuario.getId());
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
	
	
	public void logout(HttpServletRequest request) {
		String jwt=jwtService.extraerTokenDeRequest(request);  //se extrae el token de la peticion
		if (jwt==null || !StringUtils.hasText(jwt)) {
			return;
		}
		Optional<JwtToken> tokenOptional=jwtTokenRep.findByToken(jwt); //se busca el token en la tabla jwt_tokens
		if (tokenOptional.isPresent() && tokenOptional.get().isValid()) { //si el token si esta en la bd y aun es valido
			JwtToken tokenBD=tokenOptional.get();
			tokenBD.setValid(false);   //se invalida para que ya no se pueda utilizar
			jwtTokenRep.save(tokenBD);   //se guardan los cambios en la bd.
			
		}
	}

}
