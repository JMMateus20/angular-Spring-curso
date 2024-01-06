package com.bolsadeideas.springboot.backend.apirest.services.auth;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.util.Base64;


@Component
public class CryptoKeyGenerator {
	
	@Value("${security.jwt.key-size-bits}")
	private int keySizeBits;
	
	public String generateBase64CryptoKey() {
		byte[] cryptoKey=generateRandomBytes(keySizeBits / 8);
		return Base64.encode(cryptoKey).toString();
	}
	

	private byte[] generateRandomBytes(int length) {
		try {
			SecureRandom secureRandom= new SecureRandom();
			byte[] randomBytes=new byte[length];
			secureRandom.nextBytes(randomBytes);
			return randomBytes;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error al generar bytes aleatorios " + e.getLocalizedMessage() + ", " + e.getMessage());
			
		}
		
	}
			
			
			
			
	
	
	

}
