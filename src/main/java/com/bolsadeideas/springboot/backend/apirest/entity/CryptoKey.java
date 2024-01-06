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
@Table(name="crypto_keys")
public class CryptoKey {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 2048)
	private String cryptoKey;
	
	private boolean isValid;
	
	@CreationTimestamp
	private Date fechaCreacion;

	public CryptoKey(String cryptoKey, boolean isValid) {
		this.cryptoKey = cryptoKey;
		this.isValid = isValid;
	}
	
	
	

}
