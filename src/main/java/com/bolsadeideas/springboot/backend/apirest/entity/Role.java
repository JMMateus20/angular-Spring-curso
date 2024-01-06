package com.bolsadeideas.springboot.backend.apirest.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="roles")
public class Role implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String nombre;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToMany(mappedBy = "roles")
	private List<Usuario> usuarios=new ArrayList<>();
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "rol_operacion", joinColumns = @JoinColumn(name="rol_id"), inverseJoinColumns = @JoinColumn(name="operacion_id"), uniqueConstraints = {@UniqueConstraint(columnNames = {"rol_id", "operacion_id"})})
	private List<Operacion> operaciones=new ArrayList<>();
	
	
	
	
	public Role(String nombre) {
		this.nombre = nombre;
	}




	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
