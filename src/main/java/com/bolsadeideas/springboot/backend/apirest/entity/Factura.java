package com.bolsadeideas.springboot.backend.apirest.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
@Table(name="facturas")
public class Factura implements Serializable{
	
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String descripcion;
	@Column(nullable = true)
	private String observacion;
	
	@CreationTimestamp
	private Date createAt;
	
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)  //lazy solo cargará el objeto Cliente cuando se llame al getter de ese atributo
	@JoinColumn(name="cliente_id")
	private Cliente cliente;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "factura", fetch = FetchType.LAZY)
	private List<ItemFactura> items=new ArrayList<>();


	public Factura(String descripcion, String observacion, Cliente cliente) {
		this.descripcion = descripcion;
		this.observacion = observacion;
		this.cliente = cliente;
	}
	
	
	public Long calcularTotal() {
		return this.getItems().stream().mapToLong(ItemFactura::calcularImporte).sum();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	
	
	
	

}
