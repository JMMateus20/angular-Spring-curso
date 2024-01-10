package com.bolsadeideas.springboot.backend.apirest.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable, UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, length = 20)
	private String username;
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(length = 60)
	private String password;
	@JsonProperty(access = Access.WRITE_ONLY)
	private boolean enabled;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "usuario_rol", joinColumns = @JoinColumn(name="id_usuario"), inverseJoinColumns = @JoinColumn(name="id_role"), uniqueConstraints = {@UniqueConstraint(columnNames = {"id_usuario", "id_role"})})
	private List<Role> roles=new ArrayList<>();
	
	
	
	
	public Usuario(String username, String password, boolean enabled) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}



	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		//FORMA 1: OPTIMIZADA:
		
		
		
		//se utiliza Stream.concat para concatenar tanto las autoridades de roles como de operaciones	
		return this.roles.stream().flatMap(rol->Stream
				.concat(Stream.of(
						new SimpleGrantedAuthority("ROLE_"+rol.getNombre())), 
						rol.getOperaciones().stream()
						.map(op->new SimpleGrantedAuthority(op.getNombre()))))
				.distinct() //el distinct es para eliminar las operaciones duplicadas en caso que varios roles tengan una misma operacion
				.collect(Collectors.toList());
		
		//se utiliza flatMap para crear un flujo unico que recorre cada rol de la lista roles
		//convertirlo a autoridad y, enseguida, mapear cada operacion de dicho rol actual y convertirlo
		//igualmente a una autoridad
		
		
		
		//FORMA 2: LARGA
		
		/*
		
		List<SimpleGrantedAuthority> authorities= this.roles.stream()
				.map(rol->new SimpleGrantedAuthority("ROLE_"+rol.getNombre()))
				.collect(Collectors.toList());
		
		List<String> operacionesDeUsuario=this.roles.stream()
				.flatMap(rol->rol.getOperaciones() //flatmap funciona para meter un map dentro de otro
				//se crea un flujo único para cada rol y las operaciones de dicho rol
				.stream().map(Operacion::getNombre))
				.distinct()  
				.collect(Collectors.toList());
		
		
		
		List<SimpleGrantedAuthority> authoritiesOperaciones=operacionesDeUsuario.stream()
				.map(op->new SimpleGrantedAuthority(op))
				.collect(Collectors.toList());
		
		authorities.addAll(authoritiesOperaciones);
		return authorities;
		
		*/
		
		//FORMA 3: ANTIGUA (NO RECOMENDADA)
		
		/*
		
		List<SimpleGrantedAuthority> authorities=new ArrayList<>();
		for (Role rol:this.roles) {
			SimpleGrantedAuthority authority=new SimpleGrantedAuthority("ROLE_"+rol.getNombre());
			authorities.add(authority);
			for (Operacion op:rol.getOperaciones()) {
				SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(op.getNombre());
				if (!authorities.contains(simpleGrantedAuthority)) {
					authorities.add(simpleGrantedAuthority);	
				}else {
					continue;
				}
				
			}
		}
		
		return authorities;
		*/
		
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}




	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}




	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
