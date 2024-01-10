package com.bolsadeideas.springboot.backend.apirest.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="jwt_tokens")
public class JwtToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 2048)
	private String token;
	
	
	private Date expiration;
	
	
	private boolean isValid;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	public JwtToken(String token, Date expiration, boolean isValid, Usuario usuario) {
		this.token = token;
		this.expiration = expiration;
		this.isValid = isValid;
		this.usuario = usuario;
	}
	
	
	
	

}
