import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { ItemFactura } from '../item-factura';
import swal from 'sweetalert2';


@Injectable({
  providedIn: 'root'
})
export class DetalleFacturaServiceService {

  url:string="http://localhost:8080/facturas";

  constructor(private http:HttpClient) { }


  getItems(id:number):Observable<any>{
      return this.http.get<any>(this.url+`/items/${id}?page=0`);
  }


  eliminarItem(idFactura:number, idItem:number):Observable<any>{
    return this.http.delete<any>(this.url+`/items?factura=${idFactura}&item=${idItem}`).pipe(
      catchError(e=>{
        if (e.status==400) {
          swal("Error", e.error.mensaje, 'warning');
        }
        return throwError(e);
      })
    )
  }


 
}
