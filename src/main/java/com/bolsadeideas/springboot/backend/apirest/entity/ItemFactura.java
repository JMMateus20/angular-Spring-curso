package com.bolsadeideas.springboot.backend.apirest.entity;

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
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="items_factura")
public class ItemFactura {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	private Integer cantidad;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="factura_id")
	private Factura factura;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name="producto_id")
	private Producto producto;

	public ItemFactura(Integer cantidad, Factura factura, Producto producto) {
		this.cantidad = cantidad;
		this.factura = factura;
		this.producto = producto;
	}
	
	
	public Long calcularImporte() {
		return this.getProducto().getPrecio()*this.cantidad;
	}
	
	
	
	
	
	
	

}
