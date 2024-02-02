package com.bolsadeideas.springboot.backend.apirest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bolsadeideas.springboot.backend.apirest.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.entity.Factura;

public interface FacturaRepository extends JpaRepository<Factura, Long>{
	
	
	Page<Factura> findByCliente(Cliente cliente, Pageable pageable);

}
