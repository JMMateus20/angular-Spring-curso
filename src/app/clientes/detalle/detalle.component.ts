import { Component, OnInit, Input } from '@angular/core';
import { Cliente } from '../cliente';
import { ClienteService } from '../cliente.service';
import { ModalService } from './modal.service';
import { ActivatedRoute } from '@angular/router';
import swal from 'sweetalert2';
import { HttpEventType } from '@angular/common/http';
import { LoginService } from 'src/app/usuarios/login.service';
import { Factura } from '../factura';
import { tap } from 'rxjs';

@Component({
  selector: 'app-detalle',
  templateUrl: './detalle.component.html'
  
})
export class DetalleComponent implements OnInit{

  @Input() cliente: Cliente;
  public imagenSeleccionada: File;
  progreso:number=0;

  facturas: Factura[]=[];

  constructor(private clienteService: ClienteService, private activatedRoute: ActivatedRoute, public modalService: ModalService, private loginService:LoginService){}

  ngOnInit(): void {
    if (this.loginService.tienePermiso('LISTAR_FACTURAS_POR_CLIENTE')) {
      this.listarFacturas(this.cliente.id);
      
    }
    
      
    
    
  }

  seleccionarFoto(event){
     this.imagenSeleccionada=event.target.files[0];
     this.progreso=0;
     if (this.imagenSeleccionada.type.indexOf('image')<0) {                                                                             //el metodo type.indexOf('image') verifica el tipo del archivo y busca si existe un tipo relacionado a 'image' 
         swal("Error al cargar", 'La foto debe ser en formato PNG o JPG', 'error');                                                     //y retora su indice, si no encuentra 'image' devuelve -1 y quiere decir que el archivo no es una imagen
         this.imagenSeleccionada=null;
      }
  }

  subirFoto(){
    if (!this.imagenSeleccionada) {
      swal('Error al cargar', 'Debe seleccionar una foto', 'error');
    }else{
      this.clienteService.upload(this.imagenSeleccionada, this.cliente.id)
      .subscribe(event=>{
        if (event.type === HttpEventType.UploadProgress) {
          this.progreso=Math.round((event.loaded/event.total)*100);
        }else if (event.type===HttpEventType.Response) {
          let response:any = event.body;
          this.cliente=response.cliente as Cliente;
          this.modalService.notificarUpload.emit(this.cliente);
          swal('Exito', response.mensaje, 'success');
        }
        //this.cliente=cliente;                                                          //asignamos nuevamente el cliente ya que el subscribe sirve para recibir los datos
                                 // de respuesta, y el cliente entonces se devuelve pero con la foto ya asignada
      })
    }
    
  }

  cerrarModal(){
    this.modalService.cerrarModal();
    this.imagenSeleccionada=null;
    this.progreso=0;
  }


  mostrarBotones(permiso:string):boolean{
    return this.loginService.tienePermiso(permiso);
  }

  eliminarFactura(idFactura:number){
    return this.clienteService.eliminarFactura(this.cliente.id, idFactura).subscribe(
      response=>{
        swal('Exito', response.mensaje, 'success');
        this.facturas=this.facturas.filter((factura:Factura)=>factura.id!==idFactura);
      }
    )
  }

  listarFacturas(id:number):void{
    this.clienteService.listarFacturas(id).pipe(
      tap(response=>{
        
        this.facturas=response.facturas.content as Factura[];
        console.log(this.facturas);
        
        
      }
        
      )
      
    ).subscribe();
  }

}    
