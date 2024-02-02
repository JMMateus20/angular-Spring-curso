package com.bolsadeideas.springboot.backend.apirest.services;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.bolsadeideas.springboot.backend.apirest.dto.RegistroClienteDTO;
import com.bolsadeideas.springboot.backend.apirest.dto.RegistroFracturaDTO;
import com.bolsadeideas.springboot.backend.apirest.entity.Cliente;

public interface ClienteService {
	
	
	public List<Cliente> listar();
	
	//este metodo es igual al de arriba, es para listar los clientes, pero este se puede paginar
	public Page<Cliente> listar(Pageable pageable);
	
	
	ResponseEntity<?> insertar(RegistroClienteDTO datos);
	
	ResponseEntity<?> eliminar(Long id);
	
	ResponseEntity<?> encontrar(Long id);
	
	ResponseEntity<?> modificar(Long id, RegistroClienteDTO datos);
	
	ResponseEntity<?> upload(MultipartFile archivo, Long id);
	
	ResponseEntity<Resource> verFoto(String nombreFoto);
	
	
	ResponseEntity<?> agregarFactura(Long idCliente, RegistroFracturaDTO datos);
	
	ResponseEntity<?> eliminarFactura(Long idCliente, Long idFactura);

}
