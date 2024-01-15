import { Component } from '@angular/core';
import { Login } from './login';
import { Usuario } from './usuario';
import swal from 'sweetalert2';
import { LoginService } from './login.service';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  titulo:string='Por favor inicie sesion';

  credenciales:Login=new Login();
  usuario: Usuario=new Usuario();

  constructor(private loginService:LoginService, private router:Router){

  }

  ngOnInit(): void {
    if (this.loginService.estaAutenticado()) {
      swal('Login', `Usuario ${this.loginService.usuario.username} ya está autenticado`, 'info');
      this.router.navigate(['/main']);
    }
    
  }

  login():void{
    console.log(this.credenciales);
    if (this.credenciales.username==null || this.credenciales.contrasena==null) {
      swal('Error login', 'Username o password vacíos!', 'error');
      return;
    }
    this.loginService.login(this.credenciales).subscribe(
      response=>{ //se recibe el token del usuario
        //console.log(response);
        //let payload=JSON.parse(atob(response.token.split(".")[1]));  //se desencripta el token, la parte de su payload
        //console.log(payload);
        this.loginService.guardarUsuario(response.token);
        this.loginService.guardarToken(response.token);
        let usuario=this.loginService.usuario;
        this.router.navigate(['/main']);
        swal('Exito de login', `Bienvenido ${usuario.username}`, 'success'); //se muestra el subject o username del usuario
      });
    
  }

}
