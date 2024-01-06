package com.bolsadeideas.springboot.backend.apirest.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.dto.RegistroOperacionDTO;
import com.bolsadeideas.springboot.backend.apirest.entity.Modulo;
import com.bolsadeideas.springboot.backend.apirest.entity.Operacion;
import com.bolsadeideas.springboot.backend.apirest.exceptions.NotFoundExceptionManaged;
import com.bolsadeideas.springboot.backend.apirest.repository.ModuloRepository;
import com.bolsadeideas.springboot.backend.apirest.repository.OperacionRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class OperacionServiceImpl implements OperacionService{
	
	private final ModuloRepository moduloRep;
	
	private final OperacionRepository operacionRep;


	@Override
	public ResponseEntity<String> eliminar(Long operacionId) {
	
		Operacion operacionBD=this.operacionRep.findById(operacionId)
				.orElseThrow(()->new NotFoundExceptionManaged("Operacion no encontrada en la base de datos"));
		this.operacionRep.delete(operacionBD);
		
		
		return ResponseEntity.ok("Operación eliminada correctamente");
		
	}

	@Override
	public ResponseEntity<?> actualizar(Long operacionId, RegistroOperacionDTO datos) {
		Map<String, Object> respuesta=new HashMap<>();
		Operacion operacionBD=this.operacionRep.findById(operacionId)
				.orElseThrow(()->new NotFoundExceptionManaged("Operacion no encontrada en la base de datos"));
		operacionBD.setNombre(datos.getNombre());
		operacionBD.setPath(datos.getPath());
		operacionBD.setMetodo_http(datos.getMetodo_http());
		operacionBD.setPublico(datos.isPublico());
		respuesta.put("operacion", this.operacionRep.save(operacionBD));
		return ResponseEntity.ok(respuesta);
		
	}



}
