package com.bolsadeideas.springboot.backend.apirest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolsadeideas.springboot.backend.apirest.entity.JwtToken;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long>{
	
	
	
	Optional<JwtToken> findByToken(String token);

}
