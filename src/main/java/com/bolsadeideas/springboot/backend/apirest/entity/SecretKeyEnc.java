package com.bolsadeideas.springboot.backend.apirest.entity;



import java.sql.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="token_secret_key")
public class SecretKeyEnc {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(length = 32768)
	private String secretKey;
	
	
	private boolean isValid;
	
	@CreationTimestamp
	private Date fechaCreacion;

	public SecretKeyEnc(String secretKey, boolean isValid) {
		this.secretKey = secretKey;
		this.isValid = isValid;
	}
	
	
	
	
	

}
