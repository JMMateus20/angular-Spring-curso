package com.bolsadeideas.springboot.backend.apirest.auth.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bolsadeideas.springboot.backend.apirest.entity.Usuario;
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

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authorization=request.getHeader("Authorization");
		if (!StringUtils.hasText(authorization) || !authorization.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		String jwt=authorization.split(" ")[1];
		String username=jwtService.extractUsername(jwt);
		Usuario usuario=usuarioRep.findByUsername(username).get();
		UsernamePasswordAuthenticationToken upat=new UsernamePasswordAuthenticationToken(username, null, usuario.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(upat);
		filterChain.doFilter(request, response);
	}
	

}
