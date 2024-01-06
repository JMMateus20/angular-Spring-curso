package com.bolsadeideas.springboot.backend.apirest.services.auth;


import com.bolsadeideas.springboot.backend.apirest.entity.SecretKeyEnc; 

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.backend.apirest.entity.CryptoKey;
import com.bolsadeideas.springboot.backend.apirest.entity.Usuario;
import com.bolsadeideas.springboot.backend.apirest.exceptions.InternalServerExceptionManaged;
import com.bolsadeideas.springboot.backend.apirest.repository.CryptoKeyRepository;
import com.bolsadeideas.springboot.backend.apirest.repository.SecretKeyRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JWTService {
	
	@Autowired
	private CryptoKeyGenerator skg;
	
	@Autowired
	private CryptoKeyRepository cryptoRep;
	
	@Autowired
	private SecretKeyRepository secretKeyRep;
	
	@Autowired
	private CryptoService cryptoService;
	
	@Value("${security.jwt.expiration-in-minutes}")
	private Long EXPIRATION_IN_MINUTES;
	
	
	private String secretKey;
	
	
	@PostConstruct
	public void metodoPostInyeccionDeDependencias() {
		//esta crypto key es la que se usará para encriptar y desencriptar la secret_key
		//se genera aleatoriamente cada vez que se sube el servidor gracias a la clase
		//CryptoKeyGenerator
		String cryptoKey=skg.generateBase64CryptoKey();
		System.out.println("cryptokey:" + cryptoKey);
		if (cryptoRep.findAll().isEmpty()) {
			//la clave secreta o secret key en backoffice en la bd debio haber sido encriptada por primera vez usando la clave generada en la aplicación guardada a continuacion
			CryptoKey cryptoKeyNew=cryptoRep.save(new CryptoKey(cryptoKey, true));
			System.out.println("CryptoKey nueva: " + cryptoKeyNew.getCryptoKey());
			//secretKeyRep.save(new SecretKeyEnc(encrypt("aG9sYU11w7FlcXVpdG9ub3Nlbm9zZW5vc2Vuc3Nvamloemp6bnhieHhlanUzaGN1aTNoZjNvaWozZnUzOHU5MmQ5M2lpY2pjM2xqY25qM25jajN2M2pjM2p1M2hmdTNoZnUzaGZ1bzNqeGl4amlvZTJreG8yemwyd216a214Mmt4bTJlanhuMmVqamVkajJqaHU0eTc2Mjc2NzI0dGZ5Mmhocm5janJuam5jM2pjbjNqY25qY253ZGNud2N3Y3djaG53am56bmh4dnhoYnhua21ra3pta2p3aHd5cnRycXE1MzI1NDI1MjYzNzM4Mzk4MzAwMmpramRrZWtramhoYWJjZGVmZ2hpamtsbW7DsW9wcXJzdHV2d3h5eg==", cryptoKeyNew.getCryptoKey()), true)); 
			//se extrae la secretKey de los tokens desencriptandola de la base de datos usando la cryptokey guardada por primera vez anteriormente.
			secretKey=decrypt(secretKeyRep.findById(1L).get().getSecretKey(), cryptoKeyNew.getCryptoKey());
			
		}else {
			//si ya hay claves para el algoritmo bouncyCastle en la tabla crypto_keys, entonces
			//se busca el ultimo registro de la tabla crypto_keys y token_secret_key ya que 
			//quiere decir que el ultimo registro de la tabla crypto_keys es la llave maestra que
			//desencripta el ultimo registro de la tabla token_secret_key, que es el secret_key que firma los tokens
			CryptoKey cryptoEntity=this.cryptoRep.encontrarUltimo();
			SecretKeyEnc secretEntity=this.secretKeyRep.encontrarUltimo();
			if (cryptoEntity.isValid() && secretEntity.isValid()) { //si ambos registros son validos entonces
				System.out.println(secretEntity.getSecretKey());
				//se desencripta la ultima secret_key de la tabla token_secret_key usando la ultima crypto_key valida
				secretKey=decrypt(secretEntity.getSecretKey(), cryptoEntity.getCryptoKey());
				System.out.println("secret key desencriptada:" + secretKey);
				//una vez desencriptada la secret_key para su uso en las peticiones,
				//se procede a invalidar esos ultimos registros validos
				cryptoEntity.setValid(false);
				secretEntity.setValid(false);
				cryptoRep.save(cryptoEntity);
				secretKeyRep.save(secretEntity);
			}else {
				throw new InternalServerExceptionManaged("No se pudo realizar el proceso de encriptado y desencriptado de la secretKey");
			}
			//se crea nuevo registro para la tabla crypto_keys con la crypto_key que se genera aleatoriamente cada vez que se ejecuta el server
			CryptoKey cryptoKeyNew=this.cryptoRep.save(new CryptoKey(cryptoKey, true));
			//se genera un nuevo registro para la tabla token_secret_key valido, utilizando la nueva crypto_key registrada
			//para encriptar nuevamente la secret_key que firma los tokens utilizando los metodos de la clase
			//CryptoService
			this.secretKeyRep.save(new SecretKeyEnc(encrypt(secretKey, cryptoKeyNew.getCryptoKey()), true));
			
			
		}
		
		
		
		
	}
	
	
	
	public String generarToken(Usuario usuario, Map<String, Object> claims) {
		Date issuedAt=new Date(System.currentTimeMillis());
		Date expiration=new Date((EXPIRATION_IN_MINUTES*60*1000) + issuedAt.getTime());
		return Jwts.builder()
				.header().type("JWT")
				.and()
				.subject(usuario.getUsername())
				.issuedAt(issuedAt)
				.expiration(expiration)
				.claims(claims)
				.signWith(generarKey(), Jwts.SIG.HS256)
				.compact();
		
	}
	
	
	private SecretKey generarKey() {
		byte[] secretAsBytes=Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(secretAsBytes);
	}
	
	
	public String extractUsername(String jwt) {
		return extractAllClaims(jwt).getSubject();
	}
	
	
	private Claims extractAllClaims(String jwt) {
		return Jwts.parser().verifyWith(generarKey()).build()
				.parseSignedClaims(jwt).getPayload();
	}
	
	
	private String encrypt(String textoAEncriptar, String key) {
		try {
			return cryptoService.encrypt(textoAEncriptar, key);
		}catch(Exception e) {
			throw new InternalServerExceptionManaged("No se pudo encriptar la clave secreta");
		}
	}
	
	
	private String decrypt(String textoADesencriptar, String key) {
		try {
			return cryptoService.decrypt(textoADesencriptar, key);
		}catch(Exception e) {
			throw new InternalServerExceptionManaged("No se pudo desencriptar la clave secreta");
		}
	}
	
	
	
	
	
	

}
