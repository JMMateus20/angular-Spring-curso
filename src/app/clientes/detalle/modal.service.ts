import { EventEmitter, Injectable } from '@angular/core';
import { LoginService } from '../../usuarios/login.service';

@Injectable({
  providedIn: 'root'
})
export class ModalService {
  modal:boolean=false;
  notificarUpload=new EventEmitter<any>();

  constructor() { }

  abrirModal(){
    this.modal=true;
  }

  cerrarModal(){
    this.modal=false;
  }

  

  
}
