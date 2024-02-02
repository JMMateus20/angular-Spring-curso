import { Injectable } from '@angular/core';
import { Cliente } from './cliente';
import { Observable } from 'rxjs';
import { of, catchError, throwError } from 'rxjs';
import { HttpClient, HttpEvent, HttpHeaders, HttpRequest } from '@angular/common/http';
import swal from 'sweetalert2';
import { Router } from '@angular/router';
import { map } from 'rxjs';
import { formatDate } from '@angular/common';
import { tap } from 'rxjs';
import { Region } from './region';
import { LoginService } from '../usuarios/login.service';
import { FacturaRequest } from './factura-request';


@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  
  private urlEndpoint:string="http://localhost:8080/clientes";
  
  constructor(private http:HttpClient, private router: Router, private loginService: LoginService) { }

 

/*
  private agregarHeaderAuthorization(){
    let token=this.loginService.token;
    if (token!=null) {
      return this.httpHeaders.append('Authorization', 'Bearer ' + token);
    }
    return this.httpHeaders;
  }
  */


  getRegiones():Observable<any>{
      return this.http.get<any>("http://localhost:8080/regiones");
  }
  
  getClientes(page:number): Observable<any>{
    //return of(CLIENTES);
    return this.http.get(this.urlEndpoint+'/page/'+page).pipe(
      
      
      tap((response:any)=>{
        (response.content as Cliente[]).forEach(cliente=>{
        console.log(cliente.nombre);
        });
        
      }),
      map((response:any)=>{
        (response.content as Cliente[]).map(cliente=>{
          cliente.nombre=cliente.nombre.toUpperCase();
          cliente.createAt=formatDate(cliente.createAt, 'fullDate', 'es-ES');
          return cliente;
        })
        return response;
      })
    );
  }
  
  create(cliente: Cliente):Observable<Cliente>{
    return this.http.post<Cliente>(this.urlEndpoint, cliente).pipe(
      catchError(e => {
        if(e.status === 400){
          if(Array.isArray(e.error)){
              return throwError(e);
          }
          swal('Error al crear el cliente', e.error, 'error');
        }
        return throwError(e);
      })
    );
  }

  getClienteById(id:number):Observable<Cliente>{
    return this.http.get<Cliente>(`${this.urlEndpoint}/${id}`).pipe(
      catchError(e => {
        if (e.status!=401) {
          this.router.navigate(['/clientes']);
        }
        
        if (e.status === 404) {
          swal('Cliente no encontrado', e.error, 'error');
        }
        return throwError(e);
      })
    );
  }


  update(cliente: Cliente): Observable<Cliente>{
    return this.http.put<Cliente>(`${this.urlEndpoint}/${cliente.id}`, cliente).pipe(
      catchError(e => {
       
        if(e.status === 400){
          if (Array.isArray(e.error)) {
            return throwError(e);
          }
          swal('Error al actualizar el cliente', e.error, 'error');
        }
        return throwError(e);
      })
    );
  }

  delete(id:number):Observable<any>{
    return this.http.delete<any>(`${this.urlEndpoint}/${id}`).pipe(
      catchError(e => { 
        if(e.status === 500){
          swal('Error al eliminar el cliente', e.error, 'error');
        }
        return throwError(e);
      })
    );
  }

  upload(archivo:File, id):Observable<HttpEvent<{}>>{
    let formData=new FormData();
    formData.append("archivo", archivo);
    formData.append("id", id);
    /*
    let httpHeaders=new HttpHeaders();
    let token=this.loginService.token;
    if (token!=null) {
      httpHeaders=httpHeaders.append('Authorization', 'Bearer ' + token);
    }
    */

    const req=new HttpRequest('POST', `${this.urlEndpoint}/upload`, formData, {
      reportProgress:true
    });
    return this.http.request(req);
  
  }

  agregarFactura(idCliente:number, datos:FacturaRequest):Observable<any>{
    return this.http.post<any>(this.urlEndpoint+`/facturas/${idCliente}`, datos);

  }


  verificarPermiso(permiso:string):boolean{
    return this.loginService.tienePermiso(permiso);
  }


  listarFacturas(id:number):Observable<any>{
    return this.http.get<any>(`http://localhost:8080/facturas/${id}?page=0`);
  }


  eliminarFactura(idCliente:number, idFactura:number):Observable<any>{
    return this.http.delete<any>(this.urlEndpoint+`/facturas?cliente=${idCliente}&factura=${idFactura}`);
    
  }
  

  
}
