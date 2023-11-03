import { Component, OnInit } from '@angular/core';
import { CLIENTES } from './clientes.json';
import { Cliente } from './cliente';
import { ClienteService } from './cliente.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import swal from 'sweetalert2';


@Component({
  selector: 'app-clientes',
  templateUrl: './clientes.component.html',
  styleUrls: ['./clientes.component.css']
})
export class ClientesComponent implements OnInit{
  clientes: Cliente[];
  
  constructor(private clienteService: ClienteService, private router: Router, private activatedRouter: ActivatedRoute, private location: Location){}

  ngOnInit(): void {
    this.clienteService.getClientes().subscribe(
      clientes => this.clientes = clientes
    );
    
    
  }

  delete(cliente: Cliente):void{
    this.clienteService.delete(cliente.id).subscribe(
      respuesta=>{
        this.clientes=this.clientes.filter(client=>client!==cliente);
        swal('Exito!', respuesta.Exito, 'success');
        
      }
    )
  }
  

}
