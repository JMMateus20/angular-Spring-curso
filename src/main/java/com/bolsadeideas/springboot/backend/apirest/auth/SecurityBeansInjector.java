package com.bolsadeideas.springboot.backend.apirest.auth;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bolsadeideas.springboot.backend.apirest.entity.Usuario;
import com.bolsadeideas.springboot.backend.apirest.exceptions.NotFoundExceptionManaged;
import com.bolsadeideas.springboot.backend.apirest.repository.UsuarioRepository;

@Configuration
public class SecurityBeansInjector {
	
	@Autowired
	private AuthenticationConfiguration authConfig;
	
	@Autowired
	private UsuarioRepository usuarioRep;
	
	private Logger logger=LoggerFactory.getLogger(SecurityBeansInjector.class);
	
	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return this.authConfig.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();
		authProvider.setPasswordEncoder(passwordEncoder());
		authProvider.setUserDetailsService(userDetailsService());
		return authProvider;
	}
	
	@Bean
	public BouncyCastleProvider bouncyCastleProvider() {
		return new BouncyCastleProvider();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return (username)->{
			Usuario usuarioBD=usuarioRep.encontrarPorUsername(username);
			if (usuarioBD==null) {
				logger.error("Error: no existe el usuario '" + username + "' en la base de datos");
				throw new NotFoundExceptionManaged("No existe el usuario en la base de datos");
			}
			return usuarioBD;
					
		};
	}

}
