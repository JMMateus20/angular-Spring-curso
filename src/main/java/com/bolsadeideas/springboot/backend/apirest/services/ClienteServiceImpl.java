package com.bolsadeideas.springboot.backend.apirest.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bolsadeideas.springboot.backend.apirest.dto.FacturasDeClienteResponse;
import com.bolsadeideas.springboot.backend.apirest.dto.RegistroClienteDTO;
import com.bolsadeideas.springboot.backend.apirest.dto.RegistroFracturaDTO;
import com.bolsadeideas.springboot.backend.apirest.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.entity.Factura;
import com.bolsadeideas.springboot.backend.apirest.entity.Region;
import com.bolsadeideas.springboot.backend.apirest.exceptions.DataAccessExceptionManaged;
import com.bolsadeideas.springboot.backend.apirest.exceptions.NotFoundExceptionManaged;
import com.bolsadeideas.springboot.backend.apirest.repository.ClienteRepository;
import com.bolsadeideas.springboot.backend.apirest.repository.FacturaRepository;
import com.bolsadeideas.springboot.backend.apirest.repository.RegionRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ClienteServiceImpl implements ClienteService{
	
	
	private final ClienteRepository clienteRep;
	
	private final RegionRepository regionRep;
	
	private final UploadFileService uploadService;
	private final FacturaRepository facturaRep;

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> listar() {
		return (List<Cliente>) clienteRep.findAll();
	}

	@Override
	public ResponseEntity<?> insertar(RegistroClienteDTO datos) {
		Cliente clienteNew=new Cliente(datos.getNombre(), datos.getApellido(), datos.getEmail(), datos.getCreateAt(), datos.getRegion());
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
			String nombreFotoAnterior=clienteBD.getFoto();
			uploadService.eliminar(nombreFotoAnterior);
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
		clienteBD.setCreateAt(datos.getCreateAt());
		clienteBD.setRegion(datos.getRegion());
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

	@Override
	public ResponseEntity<?> upload(MultipartFile archivo, Long id) {
		Map<String, Object> response=new HashMap<>();
		Cliente clienteBD=clienteRep.findById(id).orElseThrow(()->new NotFoundExceptionManaged("cliente no encontrado"));
		if (!archivo.isEmpty()) {
			
			String nombreArchivo=null;
			try {
				nombreArchivo=uploadService.copiar(archivo);
			} catch (IOException e) {
				response.put("error", "Error, no se pudo subir la imagen");
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			String nombreFotoAnterior=clienteBD.getFoto();
			uploadService.eliminar(nombreFotoAnterior);
			clienteBD.setFoto(nombreArchivo);
			clienteRep.save(clienteBD);
			response.put("cliente", clienteBD);
			response.put("mensaje", "Imagen subida correctamente: " + nombreArchivo);
		}
		return new ResponseEntity<>(response, HttpStatus.CREATED);
		
	}

	@Override
	public ResponseEntity<Resource> verFoto(String nombreFoto) {
		
		Resource recurso=null;
		try {
			recurso=uploadService.cargar(nombreFoto);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		HttpHeaders cabecera=new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");
		return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> agregarFactura(Long idCliente, RegistroFracturaDTO datos) {
		Map<String, Object> respuesta=new HashMap<>();
		Cliente clienteBD=clienteRep.findById(idCliente).orElseThrow(()->new NotFoundExceptionManaged("cliente no encontrado"));
		Factura factura=new Factura(datos.getDescripcion(), datos.getObservacion(), clienteBD);
		clienteBD.getFacturas().add(factura);
		clienteRep.save(clienteBD);
		FacturasDeClienteResponse dto=new FacturasDeClienteResponse(factura.getId(), factura.getDescripcion(), factura.getObservacion(), factura.getCreateAt());
		respuesta.put("factura", dto);
		return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> eliminarFactura(Long idCliente, Long idFactura) {
		Map<String, Object> respuesta=new HashMap<>();
		Cliente clienteBD=clienteRep.findById(idCliente).orElseThrow(()->new NotFoundExceptionManaged("cliente no encontrado"));
		Factura facturaBD=facturaRep.findById(idFactura).orElseThrow(()->new NotFoundExceptionManaged("Factura no encontrada"));
		if (!clienteBD.getFacturas().contains(facturaBD)) {
			respuesta.put("mensaje", "este cliente no tiene la factura con id " + facturaBD.getId());
			return ResponseEntity.badRequest().body(respuesta);
		}
		clienteBD.getFacturas().remove(facturaBD);
		this.clienteRep.save(clienteBD);
		respuesta.put("mensaje", "Se ha eliminado la factura correctamente");
		return ResponseEntity.ok(respuesta);
	}

}
