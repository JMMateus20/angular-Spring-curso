package com.bolsadeideas.springboot.backend.apirest.entity;

import java.io.Serializable;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name="clientes")
public class Cliente implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	private String nombre;
	private String apellido;
	
	
	private String email;
	
	
	
	@Column(name="create_at")
	private Date createAt;
	
	
	private String foto;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "region_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Region region;

	
	public Cliente(String nombre, String apellido, String email, Date createAt, String foto) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.createAt=createAt;
		this.foto=foto;
		
	}
	
	


	public Cliente(String nombre, String apellido, String email, Date createAt, Region region) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.createAt = createAt;
		this.region=region;
	}




	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
