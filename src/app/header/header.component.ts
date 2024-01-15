import { Component } from '@angular/core'
import { LoginService } from '../usuarios/login.service';
import { Router } from '@angular/router';
import swal from 'sweetalert2';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Component({
    selector:'app-header',
    templateUrl:'./header.component.html'
})
export class HeaderComponent{
    title:string = 'App-angular';
    

    constructor(public loginService:LoginService, private router:Router, private http:HttpClient){}

    private httpHeaders=new HttpHeaders({'Content-Type': 'application/json'});

    private agregarHeaderAuthorization(){
        let token=this.loginService.token;
        if (token!=null) {
          return this.httpHeaders.append('Authorization', 'Bearer ' + token);
        }
        return this.httpHeaders;
      }

    logout(): void{
        
        this.loginService.logout().subscribe(
            response=>{
                swal('Cierre de sesión', response.mensaje, 'success');
                this.router.navigate(['/login']);
            }
        )
        
        
    }

    


    

}