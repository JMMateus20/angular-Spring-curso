import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, tap, throwError } from 'rxjs';
import { Login } from './login';
import swal from 'sweetalert2';
import {Usuario} from './usuario';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private _usuario:Usuario;
  private _token: string;

  

  constructor(private http:HttpClient) { }

  httpHeaders=new HttpHeaders({'Content-Type': 'application/json'})

  public get usuario(): Usuario{
    if (this._usuario!=null) {
      return this._usuario;
    }else if(sessionStorage.getItem('usuario')!=null && this._usuario==null){
      this._usuario=JSON.parse(sessionStorage.getItem('usuario')) as Usuario;  //se puede hacer un cast cuando pasamos de String a JSon
      return this._usuario;
    }
    return new Usuario();

  }

  public get token():string{
    if (this._token!=null) {
      return this._token;
    }else if(sessionStorage.getItem('token')!=null && this._token==null){
      this._token=sessionStorage.getItem('token');  
      return this._token;
    }
    return null;
  }

  /*
  private agregarHeaderAuthorization(){
    let token=this._token;
    if (token!=null) {
      console.log("Bearer " + token);
      return this.httpHeaders.append('Authorization', 'Bearer ' + token);
      
    }
    return this.httpHeaders;
  }
  */

  login(datos:Login):Observable<any>{
      
      const urlEndpoint="http://localhost:8080/auth/login"
      
      return this.http.post<any>(urlEndpoint, datos).pipe(
        catchError(e=>{
          if (e.status==400) {
            swal('Error al iniciar sesion', e.error.mensaje, 'error');
          }
          return throwError(e);
        })
      )
  }


  guardarUsuario(token:string):void{  //este metodo es para obtener el usuario que se encuentra autenticado, se hace a partir del token al iniciar sesion
    let payload=this.obtenerPayload(token);
    this._usuario=new Usuario();
    this._usuario.id=payload.id;
    this._usuario.username=payload.sub;
    this._usuario.roles=payload.permisos;
    sessionStorage.setItem('usuario', JSON.stringify(this._usuario));   //el stringify convierte un objeto a String y el metodo parse convierte de String a Json

  }

  guardarToken(token:string):void{
    this._token=token;
    sessionStorage.setItem('token', this._token); //se guarda el token en el sessionStorage
  }


  obtenerPayload(token:string):any{
    return JSON.parse(atob(token.split(".")[1]));
  }


  estaAutenticado(): boolean{
    if (this.token==null) {
      return false;
    }
    let payload=this.obtenerPayload(this.token);

    if (payload!=null && payload.sub && payload.sub.length>0) {
      return true;
    }
    return false;
    
    
    
  }


  logout():Observable<any>{
    return this.http.post<any>("http://localhost:8080/auth/logout", null).pipe(
      tap(()=>{
        this._token=null;
        this._usuario=null;
        sessionStorage.clear();
      }

      )
    );
  }


  tienePermiso(permiso:string):boolean{
    if (this.usuario.roles.includes(permiso)) {
      return true;
    }
    return false;
  }



  tokenExpirado():boolean{
    let token=this.token;
    let payload=this.obtenerPayload(token);
    let now=new Date().getTime()/1000;
    if (payload.exp<now) {
      return true;
    }
    return false;
  }
}
