import { CanActivateFn, Router } from '@angular/router';
import { LoginService } from '../login.service';
import { inject } from '@angular/core';
import swal from 'sweetalert2';




export const loginGuard: CanActivateFn = (route, state) => {
  
  const loginService=inject(LoginService);
  const router=inject(Router);
  if (loginService.estaAutenticado()) {
    if (loginService.tokenExpirado()) {
      console.log("token expirado, entra al guard de login");
      loginService.logout().subscribe(response=>{
        swal("Sesion expirada", "su sesión ha expirado, vuelva a iniciar sesión.", 'error');
        router.navigate(['/login']);
      })
      return false;
    }
    return true;
  }
  console.log("no esta autenticado");
  router.navigate(['/login']);
  return false;
};



