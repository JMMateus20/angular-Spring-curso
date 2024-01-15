import { Injectable } from "@angular/core";

import{
    HttpEvent, HttpInterceptor, HttpHandler, HttpRequest 
} from '@angular/common/http';

import { Observable } from 'rxjs';
import { LoginService } from "../login.service";
    
    

@Injectable()
    export class TokenInterceptor implements HttpInterceptor {
        constructor(private loginService:LoginService) {}
    
        intercept(req: HttpRequest<any>, next: HttpHandler): 
        Observable<HttpEvent<any>> {
            let token=this.loginService.token;
            if (token!=null) {
                const authReq=req.clone({
                    headers: req.headers.set('Authorization', 'Bearer ' + token)
                });
                console.log("entró al Interceptor: " + token);
                return next.handle(authReq);
            }
            return next.handle(req);
        }
    }
