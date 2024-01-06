package com.bolsadeideas.springboot.backend.apirest.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
		return this.roles.stream().map(rol->new SimpleGrantedAuthority("ROLE_"+rol.getNombre()))
				.collect(Collectors.toList());
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
