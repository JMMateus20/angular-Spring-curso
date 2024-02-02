import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DetalleFacturaServiceService } from '../detalle-factura-service.service';
import { ItemFactura } from '../../item-factura';
import swal from 'sweetalert2';
import { LoginService } from 'src/app/usuarios/login.service';

@Component({
  selector: 'app-detalle-factura',
  templateUrl: './detalle-factura.component.html',
  styleUrls: ['./detalle-factura.component.css']
})
export class DetalleFacturaComponent implements OnInit{

  id:number;
  itemsFactura:ItemFactura[];
 

  constructor(private route:ActivatedRoute, private detalleService:DetalleFacturaServiceService, private router:Router, private loginService: LoginService){}

  

  ngOnInit(): void {
    this.id = parseInt(this.route.snapshot.paramMap.get('id'));
    this.getItems(this.id);
    
  }


  getItems(id:number):void{
    this.detalleService.getItems(id).subscribe(response=>{
      this.itemsFactura=response.items.content as ItemFactura[];
    })
  }

  eliminarItem(idItem:number):void{
    console.log(idItem);
    this.detalleService.eliminarItem(this.id, idItem).subscribe(
      response=>{
        this.itemsFactura=this.itemsFactura.filter(it=>it.id!==idItem);
        swal("Item Eliminado", response.mensaje, "success");
        
        
      }
    )
  }


  mostrarBotones(permiso:string):boolean{
    return this.loginService.tienePermiso(permiso);
  }


  

}
