package com.bolsadeideas.springboot.backend.apirest.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.dto.RegistroClienteDTO;
import com.bolsadeideas.springboot.backend.apirest.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.exceptions.DataAccessExceptionManaged;
import com.bolsadeideas.springboot.backend.apirest.exceptions.NotFoundExceptionManaged;
import com.bolsadeideas.springboot.backend.apirest.repository.ClienteRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ClienteServiceImpl implements ClienteService{
	
	
	private final ClienteRepository clienteRep;

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> listar() {
		return (List<Cliente>) clienteRep.findAll();
	}

	@Override
	public ResponseEntity<?> insertar(RegistroClienteDTO datos) {
		Cliente clienteNew=new Cliente(datos.getNombre(), datos.getApellido(), datos.getEmail());
		if (clienteRep.findByNombre(datos.getNombre()).isPresent()) {
			
			return new ResponseEntity<>("Ya se encuentra un cliente registrado con este nombre", HttpStatus.BAD_REQUEST);
		}
		if (clienteRep.findByEmail(datos.getEmail()).isPresent()) {return new ResponseEntity<>("Ya se encuentra un cliente registrado con este email", HttpStatus.BAD_REQUEST);}
		try {
			clienteRep.save(clienteNew);
		}catch(DataAccessException e) {
			throw new DataAccessExceptionManaged("Error al realizar la inserción SQL");
		}
		return new ResponseEntity<>(clienteNew, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> eliminar(Long id) {
		Map<String, Object> response=new HashMap<>();
		Cliente clienteBD=clienteRep.findById(id).orElseThrow(()->new NotFoundExceptionManaged("Cliente no encontrado"));  
		try {
			clienteRep.delete(clienteBD);
		}catch(DataAccessException e) {
			throw new DataAccessExceptionManaged("Error al eliminar el cliente en la base de datos");
		}
		response.put("Exito", "Cliente " + id + " eliminado correctamente");
		return ResponseEntity.ok(response);
		
	}

	@Override
	public ResponseEntity<?> encontrar(Long id) {
		Map<String, Object> response=new HashMap<>();
		
		Optional<Cliente> cliente=null;
		try {
			cliente=clienteRep.findById(id);
		}catch(DataAccessException e) {
			response.put("Mensaje", "Error al realizar la consulta en la base de datos");
			response.put("Error:", e.getMostSpecificCause());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		if (!cliente.isPresent()) {
			return new ResponseEntity<>("Cliente no existente en la base de daros", HttpStatus.NOT_FOUND);
		}
		
		return ResponseEntity.ok(cliente.get());
		
		
	}

	@Override
	public ResponseEntity<?> modificar(Long id, RegistroClienteDTO datos) {
		Map<String, Object> response=new HashMap<>();
		Cliente clienteBD=clienteRep.findById(id).get();
		if (clienteRep.findByNombre(datos.getNombre()).isPresent()) {
			if (!(clienteRep.findByNombre(datos.getNombre()).get()==clienteBD)) {
				return new ResponseEntity<>("Ya se encuentra un cliente registrado con este nombre", HttpStatus.BAD_REQUEST);
			}
		}
			
		if (clienteRep.findByEmail(datos.getEmail()).isPresent()) {
			if (!(clienteRep.findByEmail(datos.getEmail()).get()==clienteBD)) {
				return new ResponseEntity<>("Ya se encuentra un cliente registrado con este email", HttpStatus.BAD_REQUEST);
			}
		}
			
		clienteBD.setNombre(datos.getNombre());
		clienteBD.setApellido(datos.getApellido());
		clienteBD.setEmail(datos.getEmail());
		Cliente clienteActualizado=null;
		try {
			clienteActualizado=clienteRep.save(clienteBD);
		}catch(DataAccessException e) {
			response.put("Mensaje", "error al actualizar el cliente en la base de datos");
			response.put("Error:", e.getMostSpecificCause());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.ok(clienteActualizado);
	}

	//este metodo es igual al listar normal que ya esta, pero este esta formateado para poder paginar los resultados
	@Override
	public Page<Cliente> listar(Pageable pageable) {
		return clienteRep.findAll(pageable);
	}

}
