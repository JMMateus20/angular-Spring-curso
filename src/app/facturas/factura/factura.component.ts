import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { ClienteService } from 'src/app/clientes/cliente.service';
import { FacturaRequest } from 'src/app/clientes/factura-request';
import {FormControl, FormsModule, ReactiveFormsModule} from '@angular/forms';
import { Observable, forkJoin } from 'rxjs';
import {concatMap, flatMap, map} from 'rxjs/operators';
import swal from 'sweetalert2';
import { Cliente } from 'src/app/clientes/cliente';
import { ProductoService } from '../../clientes/productos/producto.service';
import { Producto } from 'src/app/clientes/producto';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { ItemFacturaRequest } from 'src/app/clientes/item-factura-request';
import { ItemFacturaAux } from 'src/app/clientes/item-factura-aux';
import { FacturaService } from 'src/app/clientes/facturas/factura.service';


@Component({
  selector: 'app-factura',
  templateUrl: './factura.component.html',
  styleUrls: ['./factura.component.css']
  
})

//para hacer el autocomplete, debes importar todas las clases Mat en el app.module.ts y colocar en el import todas las clases

export class FacturaComponent implements OnInit{

  public datos:FacturaRequest = new FacturaRequest();
  idCliente:number=0;
  autoCompleteControl = new FormControl();
  productosFiltrados: Observable<Producto[]>;
  cliente:Cliente=new Cliente();
  items:ItemFacturaAux[]=[];
  totalFactura:number=0;
  errores:string[]=[];


  constructor(private clienteService:ClienteService, private route:ActivatedRoute, private router:Router, private productoService:ProductoService, private facturaService: FacturaService){}

  ngOnInit() {
    
    this.idCliente=parseInt(this.route.snapshot.paramMap.get('clienteId'));
    this.clienteService.getClienteById(this.idCliente).subscribe(response=>{
      this.cliente=response as Cliente;
    });

    this.productosFiltrados = this.autoCompleteControl.valueChanges.pipe(
      map(value=>typeof value === 'string'? value: value.nombre),
      flatMap(value => value ? this._filter(value):[])
    );
    
  }

  private _filter(value: string): Observable<Producto[]> {
    const filterValue = value.toLowerCase();

    return this.productoService.filtrarProductos(filterValue);
  }

  
  crearFactura():void{
    console.log(this.datos);
    this.clienteService.agregarFactura(this.idCliente, this.datos).pipe(
      concatMap((response:any)=>{
        let idFacturaNueva:number=response.factura.id as number;
        return forkJoin(
          this.items.map((item:ItemFacturaAux)=>{
            let itemNuevo:ItemFacturaRequest={
              productoId:item.producto.id,
              cantidad:item.cantidad

            };
            return this.facturaService.insertarItem(idFacturaNueva, itemNuevo);
          })
        )
      })
    ).subscribe(responses=>{
      swal("Factura insertada", "Factura insertada con éxito", 'success');
      this.router.navigate(['/clientes']);
    },
    err=>{
      if (err.status===400) {
        this.errores=err.error as string[];
      }
    });
  }

  //esta funcion sirve para mostrar el nombre del producto en el input cuando lo seleccionemos en el autocomplete
  mostrarNombre(producto?:Producto):string | undefined{   //puede o no puede recibir un objeto de tipo Producto y puede retornar o un String o indefinido 
    return producto? producto.nombre:undefined;  //si el producto si es enviado, se retorna su nombre, sino un indefinido
  }

  seleccionarProducto(event:MatAutocompleteSelectedEvent):void{
    let producto=event.option.value as Producto;
    console.log(producto);

    if (this.productoYaExistente(producto.id)) {
      this.incrementarCantidad(producto.id);
      
    }else{
      let nuevoItem=new ItemFacturaAux();
      nuevoItem.producto=producto;
      this.items.push(nuevoItem);
      
    }

    
    this.autoCompleteControl.setValue('');
    event.option.focus();
    event.option.deselect();
  }


  actualizarCantidad(id:number, event:any):void{
    let cantidad:number=event.target.value as number;
    if (cantidad==0) {
      this.eliminarItemColocado(id);
      return;
    }
    this.items=this.items.map((item:ItemFacturaAux)=>{
      if (id===item.producto.id) {
          item.cantidad=cantidad;

      }
      return item;
    });
  }


  productoYaExistente(id:number):boolean{
    let yaExiste:boolean=false
    this.items.forEach((item:ItemFacturaAux)=>{
      if (item.producto.id===id) {
        yaExiste=true;
      }
    })
    return yaExiste;
  }

  incrementarCantidad(id:number):void{
    this.items=this.items.map((item:ItemFacturaAux)=>{
      if (id===item.producto.id) {
        ++item.cantidad;
      }
      return item;
    });
  }


  eliminarItemColocado(id:number):void{
    this.items=this.items.filter((item:ItemFacturaAux)=>id!==item.producto.id);
    console.log(this.items);
  }


  calcularTotal():number{
    this.totalFactura=0;
    this.items.forEach((item:ItemFacturaAux)=>{
      this.totalFactura+=item.calcularImporte();
    })
    return this.totalFactura;
  }




}
