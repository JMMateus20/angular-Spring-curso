package com.bolsadeideas.springboot.backend.apirest.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="operaciones")
public class Operacion {  //READ_ONE_CLIENT pertenece al modulo de clientes
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	private String path;
	private String metodo_http;
	private boolean publico;
	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToOne(optional = false)
	@JoinColumn(name = "modulo_id")
	private Modulo modulo;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToMany(mappedBy = "operaciones")
	private List<Role> roles=new ArrayList<>();

	public Operacion(String nombre, String path, String metodo_http, boolean publico, Modulo modulo) {
		this.nombre = nombre;
		this.path = path;
		this.metodo_http = metodo_http;
		this.publico = publico;
		this.modulo = modulo;
	}
	
	

}
