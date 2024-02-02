package com.bolsadeideas.springboot.backend.apirest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bolsadeideas.springboot.backend.apirest.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
	
	
	
	List<Producto> findByNombreStartingWithIgnoreCase(String termino);   //la nomenclatura StartingWith sirve para lanzar registros que empiecen con un termino o letras, como ejemplo : MAR, TAL
	//y el ignore case ignora si el termino está en mayuscula o minúscula
}
