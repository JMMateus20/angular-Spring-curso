package com.bolsadeideas.springboot.backend.apirest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolsadeideas.springboot.backend.apirest.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
	Optional<Role> findByNombre(String nombre);
}
