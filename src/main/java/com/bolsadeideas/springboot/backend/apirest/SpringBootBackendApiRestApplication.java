package com.bolsadeideas.springboot.backend.apirest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bolsadeideas.springboot.backend.apirest.entity.CryptoKey;
import com.bolsadeideas.springboot.backend.apirest.entity.SecretKeyEnc;
import com.bolsadeideas.springboot.backend.apirest.repository.CryptoKeyRepository;
import com.bolsadeideas.springboot.backend.apirest.repository.SecretKeyRepository;
import com.bolsadeideas.springboot.backend.apirest.services.auth.CryptoKeyGenerator;
import com.bolsadeideas.springboot.backend.apirest.services.auth.CryptoService;

@SpringBootApplication
public class SpringBootBackendApiRestApplication{
	


	public static void main(String[] args) {
		SpringApplication.run(SpringBootBackendApiRestApplication.class, args);
	}

	

	
	

}
