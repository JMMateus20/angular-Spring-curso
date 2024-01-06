package com.bolsadeideas.springboot.backend.apirest.dto;

import java.util.ArrayList;
import java.util.List;

import com.bolsadeideas.springboot.backend.apirest.entity.Operacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperacionesPorModuloDTO {
	
	
	private String nombreModulo;
	private List<Operacion> operaciones = new ArrayList<>();

}
