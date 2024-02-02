package com.bolsadeideas.springboot.backend.apirest.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bolsadeideas.springboot.backend.apirest.entity.Factura;
import com.bolsadeideas.springboot.backend.apirest.entity.ItemFactura;
import com.bolsadeideas.springboot.backend.apirest.entity.Producto;

public interface ItemFacturaRepository extends JpaRepository<ItemFactura, Long>{
	
	
	Page<ItemFactura> findByFactura(Factura factura, Pageable pageable);
	
	Optional<ItemFactura> findByProductoAndFactura(Producto producto, Factura factura);

}
