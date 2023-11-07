package com.bolsadeideas.springboot.backend.apirest.dto;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
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
	

}
