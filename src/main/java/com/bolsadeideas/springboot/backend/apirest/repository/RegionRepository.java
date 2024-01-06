package com.bolsadeideas.springboot.backend.apirest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolsadeideas.springboot.backend.apirest.entity.Region;

public interface RegionRepository extends JpaRepository<Region, Long>{
	
	Optional<Region> findByNombre(String nombre);

}
