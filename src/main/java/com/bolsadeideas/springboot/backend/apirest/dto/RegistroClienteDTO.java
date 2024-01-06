package com.bolsadeideas.springboot.backend.apirest.dto;



import java.sql.Date;

import com.bolsadeideas.springboot.backend.apirest.entity.Region;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroClienteDTO {
	
	@NotEmpty
	@Size(min = 4, max = 12, message = "debe tener entre 4 y 12 caracteres")
	private String nombre;
	private String apellido;
	@NotEmpty
	@Email(message = "debe ser un email válido")
	private String email;
	
	@NotNull(message = "no puede estar vacio")
	private Date createAt;
	
	@NotNull(message = "no puede estar vacio")
	private Region region;
	

}
