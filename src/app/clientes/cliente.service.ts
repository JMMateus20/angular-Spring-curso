import { Injectable } from '@angular/core';
import { Cliente } from './cliente';
import { CLIENTES } from './clientes.json';
import { Observable } from 'rxjs';
import { of, catchError, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import swal from 'sweetalert2';
import { Router } from '@angular/router';
import { map } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  private httpHeaders=new HttpHeaders({'Content-Type': 'application/json'})
  private urlEndpoint:string="http://localhost:8080/clientes";
  constructor(private http:HttpClient, private router: Router) { }
  
  getClientes(): Observable<Cliente[]>{
    //return of(CLIENTES);
    return this.http.get<Cliente[]>(this.urlEndpoint);
  }
  
  create(cliente: Cliente):Observable<Cliente>{
    return this.http.post<Cliente>(this.urlEndpoint, cliente, {headers: this.httpHeaders}).pipe(
      catchError(e => {
        if(e.status === 400){
          swal('Error al crear el cliente', e.error, 'error');
        }else{
          swal('Error al crear el cliente', e.error, 'error');
        }
        return throwError(e);
      })
    );
  }

  getClienteById(id:number):Observable<Cliente>{
    return this.http.get<Cliente>(`${this.urlEndpoint}/${id}`).pipe(
      catchError(e => {
        this.router.navigate(['/clientes']);
        if (e.status === 404) {
          swal('Cliente no encontrado', e.error, 'error');
        } else {
          swal('Error al obtener cliente', e.error.Mensaje, 'error');
        }
        return throwError(e);
      })
    );
  }


  update(cliente: Cliente): Observable<Cliente>{
    return this.http.put<Cliente>(`${this.urlEndpoint}/${cliente.id}`, cliente, {headers: this.httpHeaders}).pipe(
      catchError(e => {
       
        if(e.status === 400){
          swal('Error al actualizar el cliente', e.error, 'error');
        }else{
          swal('Error al actualizar el cliente', e.error.Mensaje, 'error');
        }
        return throwError(e);
      })
    );
  }

  delete(id:number):Observable<any>{
    return this.http.delete<any>(`${this.urlEndpoint}/${id}`, {headers:this.httpHeaders}).pipe(
      catchError(e => {
        if(e.status === 500){
          swal('Error al eliminar el cliente', e.error, 'error');
        }else{
          swal('Error al eliminar el cliente', 'Error al eliminar el cliente', 'error');
        }
        return throwError(e);
      })
    );
  }
  

  
}
