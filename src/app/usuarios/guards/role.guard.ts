import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { LoginService } from '../login.service';
import swal from 'sweetalert2';

export const roleGuard: CanActivateFn = (route, state) => {
  const loginService=inject(LoginService);
  const router=inject(Router);

  if (!loginService.estaAutenticado()) {
    router.navigate(['/login']);
    return false;
  }

  let permiso=route.data['permiso'] as string;
  console.log(permiso);
  if (loginService.tienePermiso(permiso)) {
    return true;
  }
  swal("Acceso denegado", 'No tienes acceso a esta ruta', 'error');
  router.navigate(['/clientes']);
  return false;
};
