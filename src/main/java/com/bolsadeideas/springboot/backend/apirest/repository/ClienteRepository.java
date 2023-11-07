package com.bolsadeideas.springboot.backend.apirest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolsadeideas.springboot.backend.apirest.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{
	
	Optional<Cliente> findByNombre(String nombre);
	Optional<Cliente> findByEmail(String email);

}
