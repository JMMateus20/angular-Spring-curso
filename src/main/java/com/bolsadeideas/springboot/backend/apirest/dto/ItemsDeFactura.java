package com.bolsadeideas.springboot.backend.apirest.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemsDeFactura {
	
	
	private Long id;
	private String descripcion;
	private String observacion;
	private Date createAt;
	private Long total;
	private List<ItemFacturaResponseDTO> items=new ArrayList<>();

}
