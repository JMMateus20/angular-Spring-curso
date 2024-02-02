import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ItemFacturaRequest } from '../item-factura-request';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FacturaService {

  urlEndpoint:string="http://localhost:8080/facturas/items";

  constructor(private http:HttpClient) { }

  


  insertarItem(idFactura:number, item:ItemFacturaRequest):Observable<any>{
    return this.http.post<any>(this.urlEndpoint+`/${idFactura}`, item);
  }
}
