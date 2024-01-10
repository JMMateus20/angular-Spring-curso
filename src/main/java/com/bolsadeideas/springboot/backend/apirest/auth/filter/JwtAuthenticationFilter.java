package com.bolsadeideas.springboot.backend.apirest.auth.filter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bolsadeideas.springboot.backend.apirest.entity.JwtToken;
import com.bolsadeideas.springboot.backend.apirest.entity.Usuario;
import com.bolsadeideas.springboot.backend.apirest.exceptions.NotFoundExceptionManaged;
import com.bolsadeideas.springboot.backend.apirest.repository.JwtTokenRepository;
import com.bolsadeideas.springboot.backend.apirest.repository.UsuarioRepository;
import com.bolsadeideas.springboot.backend.apirest.services.auth.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private UsuarioRepository usuarioRep;
	
	@Autowired
	private JwtTokenRepository jwtTokenRep;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String jwt=jwtService.extraerTokenDeRequest(request);
		if (jwt==null || !StringUtils.hasText(jwt)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		boolean tokenIsValid=validateToken(jwtTokenRep.findByToken(jwt));
		
		if (!tokenIsValid) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String username=jwtService.extractUsername(jwt);
		Usuario usuario=usuarioRep.findByUsername(username).get();
		UsernamePasswordAuthenticationToken upat=new UsernamePasswordAuthenticationToken(username, null, usuario.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(upat);
		filterChain.doFilter(request, response);
	}
	
	
	private void actualizarEstadoDelToken(JwtToken tokenBD) {
		tokenBD.setValid(false);
		jwtTokenRep.save(tokenBD);
	}
	
	
	private boolean validateToken(Optional<JwtToken> tokenOptional) {
		if (!tokenOptional.isPresent()) {
			return false;
		}
		JwtToken tokenBD=tokenOptional.get();
		//si el token aun sigue siendo valido en la bd y no se ha hecho logout, y su fecha de expiracion esta después de la fecha de ahora, es válido
		boolean isValid=tokenBD.isValid() && tokenBD.getExpiration().after(new Date(System.currentTimeMillis()));
		
		if (!isValid) {
			actualizarEstadoDelToken(tokenBD);
		}
		return isValid;
		
	}
	

}
