package com.bolsadeideas.springboot.backend.apirest.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.backend.apirest.dto.RegistroRoleDTO;
import com.bolsadeideas.springboot.backend.apirest.entity.Role;
import com.bolsadeideas.springboot.backend.apirest.entity.Usuario;
import com.bolsadeideas.springboot.backend.apirest.exceptions.NotFoundExceptionManaged;
import com.bolsadeideas.springboot.backend.apirest.repository.OperacionRepository;
import com.bolsadeideas.springboot.backend.apirest.repository.RoleRepository;
import com.bolsadeideas.springboot.backend.apirest.entity.Operacion;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class RoleServiceImpl implements RoleService{
	
	
	private final RoleRepository roleRep;
	private final OperacionRepository operacionRep;

	@Override
	public ResponseEntity<?> insertar(RegistroRoleDTO datos) {
		Map<String, Object> respuesta=new HashMap<>();
		if (roleRep.findByNombre(datos.getNombre()).isPresent()) {
			respuesta.put("mensaje", "este rol ya se encuentra registrado");
			return ResponseEntity.badRequest().body(respuesta);
		}
		Role roleNew=new Role(datos.getNombre());
		respuesta.put("role", roleRep.save(roleNew));
		return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<List<Usuario>> verUsuarios(Long idRole) {
		Role rolBD=roleRep.findById(idRole).orElseThrow(()->new NotFoundExceptionManaged("Rol no encontrado"));
		if (!rolBD.getUsuarios().isEmpty()) {
			return ResponseEntity.ok(rolBD.getUsuarios());
		}
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<?> agregarOperacion(Long idRole, Long idOperacion) {
		Map<String, Object> respuesta=new HashMap<>();
		Role rolBD=roleRep.findById(idRole).orElseThrow(()->new NotFoundExceptionManaged("Rol no encontrado"));
		Operacion operacionBD=this.operacionRep.findById(idOperacion).orElseThrow(()->new NotFoundExceptionManaged("Operacion no encontrada en la base de datos"));
		if (rolBD.getOperaciones().contains(operacionBD)) {
			respuesta.put("mensaje", "Este rol ya contiene la operación '" + operacionBD.getNombre() + "' ");
			return ResponseEntity.badRequest().body(respuesta);
		}
		rolBD.getOperaciones().add(operacionBD);
		respuesta.put("rol", this.roleRep.save(rolBD));
		return ResponseEntity.ok(respuesta);
	}

}
