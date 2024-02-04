import { Component, OnInit } from '@angular/core';
import { Cliente } from './cliente';
import { ClienteService } from './cliente.service';
import { ModalService } from './detalle/modal.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import swal from 'sweetalert2';
import { tap } from 'rxjs';
import { Factura } from './factura';
import { LoginService } from '../usuarios/login.service';



@Component({
  selector: 'app-clientes',
  templateUrl: './clientes.component.html',
  styleUrls: ['./clientes.component.css']
})
export class ClientesComponent implements OnInit{
  clientes: Cliente[]=[];
  
  paginador:any;
  clienteSeleccionado: Cliente;
  
  constructor(private clienteService: ClienteService, private router: Router, private activatedRouter: ActivatedRoute, private location: Location, public modalService: ModalService, public loginService:LoginService){}

  ngOnInit(): void {
    this.activatedRouter.paramMap.subscribe( params=>{
        let page: number= +params.get("page");
        if(!page){
          page=0;
        }
        this.clienteService.getClientes(page).pipe(
          tap(response=>{
            this.clientes=response.content as Cliente[];
            this.paginador=response;
          })
        ).subscribe();
        
    });
    this.modalService.notificarUpload.subscribe(cliente=>{
      this.clientes=this.clientes.map(clienteOriginal=>{
        if (cliente.id==clienteOriginal.id) {
          clienteOriginal.foto=cliente.foto;
        }
        return clienteOriginal;
      })
    })
  }

  delete(cliente: Cliente):void{
    this.clienteService.delete(cliente.id).subscribe(
      respuesta=>{
        this.clientes=this.clientes.filter(client=>client!==cliente);
        swal('Exito!', respuesta.Exito, 'success');
        
      }
    )
  }

  abrirModal(cliente: Cliente){
    this.clienteSeleccionado=cliente;
    this.modalService.abrirModal();
  }


  mostrarContenido(permiso:string):boolean{
    return this.clienteService.verificarPermiso(permiso);
  }

  


  

}
