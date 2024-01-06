package com.bolsadeideas.springboot.backend.apirest.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.dto.OperacionesPorModuloDTO;
import com.bolsadeideas.springboot.backend.apirest.dto.RegistroModuloDTO;
import com.bolsadeideas.springboot.backend.apirest.dto.RegistroOperacionDTO;
import com.bolsadeideas.springboot.backend.apirest.entity.Modulo;
import com.bolsadeideas.springboot.backend.apirest.entity.Operacion;
import com.bolsadeideas.springboot.backend.apirest.exceptions.NotFoundExceptionManaged;
import com.bolsadeideas.springboot.backend.apirest.repository.ModuloRepository;
import com.bolsadeideas.springboot.backend.apirest.repository.OperacionRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ModuloServiceImpl implements ModuloService{
	
	
	private final ModuloRepository moduloRep;
	private final OperacionRepository operacionRep;

	@Override
	public ResponseEntity<?> insertar(RegistroModuloDTO datos) {
		Map<String, Object> respuesta=new HashMap<>();
		Modulo modulo=new Modulo(datos.getNombre(), datos.getPath_base());
		respuesta.put("modulo", moduloRep.save(modulo));
		return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
		
	}


	@Override
	public ResponseEntity<?> agregarOperacion(Long moduloId, RegistroOperacionDTO datos) {
		Map<String, Object> respuesta=new HashMap<>();
		Modulo moduloBD=this.moduloRep.findById(moduloId)
				.orElseThrow(()->new NotFoundExceptionManaged("Modulo no encontrado en la base de datos"));
		Operacion operacion=new Operacion(datos.getNombre(), datos.getPath(), datos.getMetodo_http(), datos.isPublico(), moduloBD);
		moduloBD.getOperaciones().add(operacion);
		this.moduloRep.save(moduloBD);
		OperacionesPorModuloDTO dto=new OperacionesPorModuloDTO(moduloBD.getNombre(), moduloBD.getOperaciones());
		respuesta.put("respuesta", dto);
		return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
	}


	@Override
	public ResponseEntity<?> eliminarOperacion(Long moduloId, Long operacionId) {
		Map<String, Object> respuesta=new HashMap<>();
		Modulo moduloBD=moduloRep.findById(moduloId)
				.orElseThrow(()->new NotFoundExceptionManaged("Modulo no encontrado en la base de datos"));
		Operacion operacionBD=operacionRep.findById(operacionId)
				.orElseThrow(()->new NotFoundExceptionManaged("Operación no encontrada en la base de datos"));
		if (!moduloBD.getOperaciones().contains(operacionBD)) {
			throw new NotFoundExceptionManaged("Este modulo no contiene la operación: " + operacionBD.getNombre());
		}
		moduloBD.getOperaciones().remove(operacionBD);
		this.moduloRep.save(moduloBD);
		OperacionesPorModuloDTO dto=new OperacionesPorModuloDTO(moduloBD.getNombre(), moduloBD.getOperaciones());
		respuesta.put("respuesta", dto);
		return ResponseEntity.ok(respuesta);
		
	}


	@Override
	public ResponseEntity<?> listarOperaciones(Long moduloId, Pageable pageable) {
		Map<String, Object> respuesta=new HashMap<>();
		Modulo moduloBD=this.moduloRep.findById(moduloId)
				.orElseThrow(()->new NotFoundExceptionManaged("Modulo no encontrado en la base de datos"));
		Page<Operacion> operacionesDeModulo=this.operacionRep.listarPorModulo(moduloBD, pageable);
		if (!operacionesDeModulo.hasContent()) {
			return ResponseEntity.noContent().build();
		}
		respuesta.put("operaciones", operacionesDeModulo);
		return ResponseEntity.ok(respuesta);
		
	}

	@Override
	public ResponseEntity<?> listarOperaciones2(Long moduloId) {
		Map<String, Object> respuesta=new HashMap<>();
		Modulo moduloBD=this.moduloRep.findById(moduloId)
				.orElseThrow(()->new NotFoundExceptionManaged("Modulo no encontrado en la base de datos"));
		respuesta.put("operaciones", moduloBD.getOperaciones());
		return ResponseEntity.ok(respuesta);
	}

	@Override
	public ResponseEntity<?> actualizarOperacion(Long moduloId, Long operacionId, RegistroOperacionDTO datos) {
		Map<String, Object> respuesta=new HashMap<>();
		Modulo moduloBD=this.moduloRep.findById(moduloId)
				.orElseThrow(()->new NotFoundExceptionManaged("Modulo no encontrado en la base de datos"));
		Operacion operacionBD=operacionRep.findById(operacionId)
				.orElseThrow(()->new NotFoundExceptionManaged("Operación no encontrada en la base de datos"));
		if (!moduloBD.getOperaciones().contains(operacionBD)) {
			throw new NotFoundExceptionManaged("Este módulo no contiene la operacion: " + operacionBD.getNombre());
		}
		int indexOf=moduloBD.getOperaciones().indexOf(operacionBD);
		operacionBD.setNombre(datos.getNombre());
		operacionBD.setPath(datos.getPath());
		operacionBD.setMetodo_http(datos.getMetodo_http());
		operacionBD.setPublico(datos.isPublico());
		moduloBD.getOperaciones().set(indexOf, operacionBD);
		this.moduloRep.save(moduloBD);
		OperacionesPorModuloDTO dto=new OperacionesPorModuloDTO(moduloBD.getNombre(), moduloBD.getOperaciones());
		respuesta.put("respuesta", dto);
		return ResponseEntity.ok(respuesta);
	}


	@Override
	public ResponseEntity<?> modificarModulo(Long moduloId, RegistroModuloDTO datos) {
		Map<String, Object> respuesta=new HashMap<>();
		Modulo moduloBD=this.moduloRep.findById(moduloId)
				.orElseThrow(()->new NotFoundExceptionManaged("Modulo no encontrado en la base de datos"));
		moduloBD.setNombre(datos.getNombre());
		moduloBD.setPath_base(datos.getPath_base());
		respuesta.put("modulo", this.moduloRep.save(moduloBD));
		return ResponseEntity.ok(respuesta);
		
	}
	
	

	
	

}
