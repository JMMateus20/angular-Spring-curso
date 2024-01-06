package com.bolsadeideas.springboot.backend.apirest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bolsadeideas.springboot.backend.apirest.entity.CryptoKey;

public interface CryptoKeyRepository extends JpaRepository<CryptoKey, Long>{
	
	
	//en jpa al ordenar registros de forma descendente traerá el primero que encuentra al ordenarlos y usar el limit, es decir, el ultimo id de la tabla
	@Query("SELECT C FROM CryptoKey AS C ORDER BY C.id DESC LIMIT 1")
	CryptoKey encontrarUltimo();
	
	


}
