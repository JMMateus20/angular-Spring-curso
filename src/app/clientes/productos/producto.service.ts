import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Producto } from '../producto';

@Injectable({
  providedIn: 'root'
})
export class ProductoService {

  constructor(private http: HttpClient) { }


  filtrarProductos(termino:string):Observable<Producto[]>{
    return this.http.get<Producto[]>("http://localhost:8080/productos"+`/${termino}`);
  }  
}
