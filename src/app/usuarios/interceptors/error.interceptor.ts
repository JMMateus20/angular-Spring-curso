import { Injectable } from "@angular/core";

import{
    HttpEvent, HttpInterceptor, HttpHandler, HttpRequest 
} from '@angular/common/http';

import { Observable } from 'rxjs';
import { LoginService } from "../login.service";
import swal from "sweetalert2";
import { catchError } from "rxjs";
import { throwError } from "rxjs";
import { Router } from "@angular/router";
    
    

@Injectable()
    export class ErrorInterceptor implements HttpInterceptor {
        constructor(private loginService:LoginService, private router:Router) {}
    
        intercept(req: HttpRequest<any>, next: HttpHandler): 
        Observable<HttpEvent<any>> {
            
            return next.handle(req).pipe(
                catchError(e=>{
                    if (e.status==401) {

                        if (this.loginService.estaAutenticado()) {
                          this.loginService.logout().subscribe(response=>{
                            console.log("error de estado 401 auntenticado pero token expirado: error interceptor");
                            swal('Sesion expirada', 'Su tiempo de sesión ha expirado, vuelva a iniciar sesión', 'error');
                            this.router.navigate(['/login']);
                          })
                          return throwError(e);
                        }
                        console.log("error de estado 401 sin autenticar: error interceptor")
                        swal('Error de seguridad', e.error, 'error');
                        this.router.navigate(['/login'])
                        return throwError(e);
                        
                    }else if(e.status==403){
                        console.log("error de estado 403 sin permisos: error interceptor")
                        swal('Error de seguridad', e.error, 'error');
                        
                    }
                    return throwError(e);
                     
                })
            );
        }
    }