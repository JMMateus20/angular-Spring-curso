package com.bolsadeideas.springboot.backend.apirest.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.backend.apirest.entity.Producto;
import com.bolsadeideas.springboot.backend.apirest.repository.ProductoRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProductoServiceImpl implements ProductoService{
	
	
	private final ProductoRepository productoRep;

	@Override
	public ResponseEntity<?> listarNombreDeProductos(String termino) {
		
		List<Producto> productos=this.productoRep.findByNombreStartingWithIgnoreCase(termino);
		
		return ResponseEntity.ok(productos);
 		
	}

}
