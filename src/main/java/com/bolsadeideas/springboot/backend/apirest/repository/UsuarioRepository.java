package com.bolsadeideas.springboot.backend.apirest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bolsadeideas.springboot.backend.apirest.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	Optional<Usuario> findByUsername(String username);
	
	@Query("SELECT U FROM Usuario AS U WHERE U.username=:username")
	Usuario encontrarPorUsername(@Param(value = "username") String username);

}
