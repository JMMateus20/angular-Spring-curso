package com.bolsadeideas.springboot.backend.apirest.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.backend.apirest.dto.ItemFacturaResponseDTO;
import com.bolsadeideas.springboot.backend.apirest.dto.ItemsDeFactura;
import com.bolsadeideas.springboot.backend.apirest.dto.RegistroItemFacturaDTO;
import com.bolsadeideas.springboot.backend.apirest.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.entity.Factura;
import com.bolsadeideas.springboot.backend.apirest.entity.ItemFactura;
import com.bolsadeideas.springboot.backend.apirest.entity.Producto;
import com.bolsadeideas.springboot.backend.apirest.exceptions.NotFoundExceptionManaged;
import com.bolsadeideas.springboot.backend.apirest.repository.ClienteRepository;
import com.bolsadeideas.springboot.backend.apirest.repository.FacturaRepository;
import com.bolsadeideas.springboot.backend.apirest.repository.ItemFacturaRepository;
import com.bolsadeideas.springboot.backend.apirest.repository.ProductoRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class FacturaServiceImpl implements FacturaService{
	
	
	private final FacturaRepository facturaRep;
	private final ItemFacturaRepository itemRep;
	private final ClienteRepository clienteRep;
	private final ProductoRepository productoRep;

	@Override
	public ResponseEntity<?> listarPorCliente(Long idCliente, Pageable pageable) {
		Map<String, Object> respuesta=new HashMap<>();
		Cliente clienteBD=clienteRep.findById(idCliente).orElseThrow(()->new NotFoundExceptionManaged("Cliente no encontrado en la base de datos"));
		Page<Factura> facturas=facturaRep.findByCliente(clienteBD, pageable);
		List<ItemsDeFactura> listPage=facturas.getContent().stream().map(fac->new ItemsDeFactura(fac.getId(), fac.getDescripcion(), fac.getObservacion(), fac.getCreateAt(), fac.calcularTotal() ,fac.getItems().stream().map(it->new ItemFacturaResponseDTO(it.getId(), it.getCantidad(), it.getProducto().getNombre(), it.getProducto().getPrecio())).collect(Collectors.toList()))).collect(Collectors.toList());
		Page<ItemsDeFactura> resp=new PageImpl<>(listPage, pageable, facturas.getTotalElements());
		respuesta.put("facturas", resp);
		return ResponseEntity.ok(respuesta);
		
	}

	@Override
	public ResponseEntity<?> agregarItem(Long idFactura, RegistroItemFacturaDTO datos) {
		Map<String, Object> respuesta=new HashMap<>();
		Factura facturaBD=facturaRep.findById(idFactura).orElseThrow(()->new NotFoundExceptionManaged("Factura no encontrada"));
		Producto productoBD=productoRep.findById(datos.getProductoId()).orElseThrow(()->new NotFoundExceptionManaged("producto no encontrado"));
		ItemFactura item=new ItemFactura(datos.getCantidad(), facturaBD, productoBD);
		Optional<ItemFactura> itemYaExistente=itemRep.findByProductoAndFactura(productoBD, facturaBD);
		if (itemYaExistente.isPresent()) {
			respuesta.put("mensaje", "El producto " + itemYaExistente.get().getProducto().getNombre() + " intenta ser colocado dos veces.");
			return ResponseEntity.badRequest().body(respuesta);
		}
		facturaBD.getItems().add(item);
		facturaRep.save(facturaBD);
		ItemsDeFactura resp=new ItemsDeFactura(facturaBD.getId(), facturaBD.getDescripcion(), facturaBD.getObservacion(), facturaBD.getCreateAt(), facturaBD.calcularTotal() , facturaBD.getItems().stream().map(i->new ItemFacturaResponseDTO(i.getId(), i.getCantidad(), i.getProducto().getNombre(), i.getProducto().getPrecio())).collect(Collectors.toList()));
		respuesta.put("factura", resp);
		respuesta.put("total", facturaBD.calcularTotal());
		return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<?> listarItems(Long idFactura, Pageable pageable) {
		Map<String, Object> respuesta=new HashMap<>();
		Factura facturaBD=facturaRep.findById(idFactura).orElseThrow(()->new NotFoundExceptionManaged("factura no encontrada"));
		Page<ItemFactura> pageItems=itemRep.findByFactura(facturaBD, pageable);
		List<ItemFacturaResponseDTO> listItems=pageItems.getContent().stream().map(it->
			new ItemFacturaResponseDTO(it.getId(), it.getCantidad(), it.getProducto().getNombre(), it.getProducto().getPrecio())
		).collect(Collectors.toList());
		Page<ItemFacturaResponseDTO> items=new PageImpl<>(listItems, pageable, pageItems.getTotalElements());
		respuesta.put("items", items);
		return ResponseEntity.ok(respuesta);
		
	}
	
	@Override
	public ResponseEntity<?> eliminarItem(Long idFactura, Long idItem){
		Map<String, Object> respuesta=new HashMap<>();
		Factura facturaBD=facturaRep.findById(idFactura).orElseThrow(()->new NotFoundExceptionManaged("factura no encontrada"));
		ItemFactura itemBD=itemRep.findById(idItem).orElseThrow(()->new NotFoundExceptionManaged("Item no encontrado"));
		if (facturaBD.getItems().size()==1) {
			respuesta.put("mensaje", "La factura debe tener al menos un item, considere eliminar la factura para eliminar todos sus items");
			return ResponseEntity.badRequest().body(respuesta);
		}
		facturaBD.getItems().remove(itemBD);
		facturaRep.save(facturaBD);
		respuesta.put("mensaje", "Item eliminado correctamente");
		return ResponseEntity.ok(respuesta);
	}

	@Override
	public ResponseEntity<?> encontrarItem(Long idFactura, Long idItem) {
		Map<String, Object> respuesta=new HashMap<>();
		Factura facturaBD=facturaRep.findById(idFactura).orElseThrow(()->new NotFoundExceptionManaged("factura no encontrada"));
		ItemFactura itemBD=itemRep.findById(idItem).orElseThrow(()->new NotFoundExceptionManaged("Item no encontrado"));
		int index=facturaBD.getItems().indexOf(itemBD);
		ItemFactura itemBuscado=facturaBD.getItems().get(index);
		ItemFacturaResponseDTO resp=new ItemFacturaResponseDTO(itemBuscado.getId(), itemBuscado.getCantidad(), itemBuscado.getProducto().getNombre(), itemBuscado.getProducto().getPrecio());
		respuesta.put("item", resp);
		return ResponseEntity.ok(respuesta);
	}

}
