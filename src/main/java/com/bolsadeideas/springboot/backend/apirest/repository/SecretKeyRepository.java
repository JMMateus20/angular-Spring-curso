package com.bolsadeideas.springboot.backend.apirest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bolsadeideas.springboot.backend.apirest.entity.SecretKeyEnc;

public interface SecretKeyRepository extends JpaRepository<SecretKeyEnc, Long>{
	
	
	@Query("SELECT sk FROM SecretKeyEnc AS sk ORDER BY sk.id DESC LIMIT 1")
	SecretKeyEnc encontrarUltimo();
	
	
	

}
