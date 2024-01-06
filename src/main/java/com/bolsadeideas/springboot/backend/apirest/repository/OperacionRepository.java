package com.bolsadeideas.springboot.backend.apirest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bolsadeideas.springboot.backend.apirest.entity.Modulo;
import com.bolsadeideas.springboot.backend.apirest.entity.Operacion;

public interface OperacionRepository extends JpaRepository<Operacion, Long> {
	
	
	@Query("SELECT O FROM Operacion as O WHERE O.modulo=:modulo")
	Page<Operacion> listarPorModulo(@Param(value="modulo") Modulo modulo, Pageable pageable);

}
