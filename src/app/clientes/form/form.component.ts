import { Component, OnInit } from '@angular/core';
import { Cliente } from '../cliente';
import { ClienteService } from '../cliente.service';
import { Router, ActivatedRoute } from '@angular/router';
import swal from 'sweetalert2';
import { Region } from '../region';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css']
})
export class FormComponent implements OnInit{
 

  constructor(private clienteService: ClienteService, private router: Router, private activatedRoute: ActivatedRoute){}

  public cliente:Cliente = new Cliente();
  public titulo:String="Agregar cliente";
  public errores:string[];
  regiones:Region[];

  ngOnInit(): void {
    this.cargarCliente();
    this.clienteService.getRegiones().subscribe(response=>{
      this.regiones=response.regiones as Region[];
    })
  }

  cargarCliente():void{
    this.activatedRoute.params.subscribe(params=>{
      let id=params['id']
      if(id){
        this.clienteService.getClienteById(id).subscribe((cliente) =>this.cliente=cliente

        )
      }
    })
  }

  create():void{
    this.clienteService.create(this.cliente).subscribe(
      response=>{
        this.router.navigate(['/clientes'])
        swal('Cliente registrado!', `Cliente ${ this.cliente.nombre } registrado con éxito`, 'success');
      },
      err=>{
        this.errores=err.error as string[];
        
      }
      
    )
  }

  update():void{
    this.clienteService.update(this.cliente).subscribe(
      cliente=>{
        this.router.navigate(['/clientes'])
        swal('Cliente actualizado!', `Cliente ${ this.cliente.id } actualizado correctamente`, 'success')
      },
      err=>{
        this.errores=err.error as string[];
      }
    )
  }

  compararRegion(o1:Region, o2:Region){
    if (o1===undefined && o2===undefined) {
      return true
    }
    return o1==null || o2==null?false:o1.id===o2.id;
  }

 


  

  
  





  

}
