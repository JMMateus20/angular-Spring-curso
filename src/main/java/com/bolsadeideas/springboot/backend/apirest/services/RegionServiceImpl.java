package com.bolsadeideas.springboot.backend.apirest.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.backend.apirest.dto.ClienteResponseDTO;
import com.bolsadeideas.springboot.backend.apirest.dto.RegistroRegionDTO;
import com.bolsadeideas.springboot.backend.apirest.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.entity.Region;
import com.bolsadeideas.springboot.backend.apirest.exceptions.NotFoundExceptionManaged;
import com.bolsadeideas.springboot.backend.apirest.repository.RegionRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RegionServiceImpl implements RegionService{
	
	
	private final RegionRepository regionRep;
	

	@Override
	public ResponseEntity<?> insertar(RegistroRegionDTO datos) {
		Map<String, Object> respuesta=new HashMap<>();
		if (regionRep.findByNombre(datos.getNombre()).isPresent()) {
			return new ResponseEntity<>("Ya se encuentra esta región", HttpStatus.BAD_REQUEST);
		}
		Region regionNew=new Region(datos.getNombre());
		try {
			regionRep.save(regionNew);
		}catch(DataAccessException e) {
			respuesta.put("error", "No se pudo realizar la inserción en la base de datos");
			respuesta.put("detalle", e.getMostSpecificCause());
			return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		respuesta.put("region", regionNew);
		return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
	}


	@Override
	public ResponseEntity<?> listar() {
		Map<String, Object> respuesta=new HashMap<>();
		List<Region> regiones=regionRep.findAll();
		if (regiones.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		respuesta.put("regiones", regiones);
		return ResponseEntity.ok(respuesta);
	}


	@Override
	public ResponseEntity<?> listarClientes(Long id) {
		Map<String, Object> respuesta=new HashMap<>();
		Region regionBD=regionRep.findById(id).orElseThrow(()->new NotFoundExceptionManaged("Región no encontrada"));
		if (regionBD.getClientes().isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		List<ClienteResponseDTO> clientes=regionBD.getClientes().stream().map(cliente->{
			return new ClienteResponseDTO(cliente.getId(), cliente.getNombre(), cliente.getApellido(), cliente.getEmail());
		}).collect(Collectors.toList());
		respuesta.put("clientes", clientes);
		return ResponseEntity.ok(respuesta);
	}
	
	
	

}
