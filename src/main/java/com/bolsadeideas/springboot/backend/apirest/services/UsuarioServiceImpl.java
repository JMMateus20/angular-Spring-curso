package com.bolsadeideas.springboot.backend.apirest.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.backend.apirest.dto.RegistroUsuarioDTO;
import com.bolsadeideas.springboot.backend.apirest.entity.Role;
import com.bolsadeideas.springboot.backend.apirest.entity.Usuario;
import com.bolsadeideas.springboot.backend.apirest.exceptions.NotFoundExceptionManaged;
import com.bolsadeideas.springboot.backend.apirest.repository.RoleRepository;
import com.bolsadeideas.springboot.backend.apirest.repository.UsuarioRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UsuarioServiceImpl implements UsuarioService{
	
	
	private final UsuarioRepository usuarioRep;
	
	private final RoleRepository roleRep;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	

	@Override
	public ResponseEntity<?> insertar(RegistroUsuarioDTO datos) {
		Map<String, Object> respuesta=new HashMap<>();
		if (usuarioRep.findByUsername(datos.getUsername()).isPresent()) {
			respuesta.put("mensaje", "ya existe un usuario con este username");
			return ResponseEntity.badRequest().body(respuesta);
		}
		String passwordEncoded=this.passwordEncoder.encode(datos.getPassword());
		Usuario usuarioNew=new Usuario(datos.getUsername(), passwordEncoded, true);
		usuarioNew.getRoles().add(this.roleRep.findById(4L).get());
		respuesta.put("usuario", usuarioRep.save(usuarioNew));
		return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> agregarRol(Long idUsuario, Long idRol) {
		Map<String, Object> respuesta=new HashMap<>();
		Usuario usuario=usuarioRep.findById(idUsuario).orElseThrow(()->new NotFoundExceptionManaged("Usuario no encontrado"));
		Role rol=roleRep.findById(idRol).orElseThrow(()->new NotFoundExceptionManaged("Rol no encontrado"));
		usuario.getRoles().add(rol);
		usuarioRep.save(usuario);
		respuesta.put("usuario", usuario);
		return ResponseEntity.ok(respuesta);
	}

	@Override
	public ResponseEntity<List<Role>> verRoles(Long idUsuario) {
		Usuario usuario=usuarioRep.findById(idUsuario).get();
		if (!usuario.getRoles().isEmpty()) {
			return ResponseEntity.ok(usuario.getRoles());
		}
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<?> eliminarRol(Long idUsuario, Long idRol) {
		Map<String, Object> respuesta=new HashMap<>();
		Usuario usuario=usuarioRep.findById(idUsuario).orElseThrow(()->new NotFoundExceptionManaged("Usuario no encontrado"));
		Role rol=roleRep.findById(idRol).orElseThrow(()->new NotFoundExceptionManaged("Rol no encontrado"));
		usuario.getRoles().remove(rol);
		usuarioRep.save(usuario);
		respuesta.put("exito", "Rol eliminado correctamente");
		return ResponseEntity.ok(respuesta);
	}

	

}
