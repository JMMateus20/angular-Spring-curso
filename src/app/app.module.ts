import { Component, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';



import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { DirectivaComponent } from './directiva/directiva.component';
import { ClientesComponent } from './clientes/clientes.component';
import { ClienteService } from './clientes/cliente.service';
import { RouterModule, Routes } from '@angular/router';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { FormComponent } from './clientes/form/form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { registerLocaleData } from '@angular/common';
import localeES from '@angular/common/locales/es';
import { PaginatorComponent } from './paginator/paginator.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatMomentDateModule } from '@angular/material-moment-adapter';
import { MatInputModule } from '@angular/material/input';
import { DetalleComponent } from './clientes/detalle/detalle.component';
import { LoginComponent } from './usuarios/login.component';
import { MainComponent } from './principal/main/main.component';
import { loginGuard } from './usuarios/guards/login.guard';
import { roleGuard } from './usuarios/guards/role.guard';
import { TokenInterceptor } from './usuarios/interceptors/token.interceptor';
import { ErrorInterceptor } from './usuarios/interceptors/error.interceptor';
import { DetalleFacturaComponent } from './clientes/detalle/detalle-factura/detalle-factura.component';
import { FacturaComponent } from './facturas/factura/factura.component';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatFormFieldModule} from '@angular/material/form-field';





registerLocaleData(localeES, 'es');

const routes: Routes=[
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  {path: 'directivas', component: DirectivaComponent},
  {path: 'clientes', component: ClientesComponent},
  {path: 'clientes/form', component: FormComponent, canActivate:[loginGuard, roleGuard], data:{permiso: 'LISTAR_TODAS_LAS_REGIONES'}},
  {path: 'clientes/form/:id', component: FormComponent},
  {path: 'clientes/page/:page', component: ClientesComponent},
  {path: 'clientes/upload/:id', component: DetalleComponent},
  {path: 'login', component: LoginComponent},
  {path: 'main', component: MainComponent},
  {path: 'detalle/facturas/:id', component: DetalleFacturaComponent},
  {path: 'facturas/form/:clienteId', component: FacturaComponent}
]

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    DirectivaComponent,
    ClientesComponent,
    FormComponent,
    PaginatorComponent,
    DetalleComponent,
    LoginComponent,
    MainComponent,
    DetalleFacturaComponent,
    FacturaComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    RouterModule.forRoot(routes),
    BrowserAnimationsModule,
    MatDatepickerModule,
    MatMomentDateModule,
    MatInputModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    MatInputModule,
    MatFormFieldModule
  ],
  providers: [ClienteService, 
    {provide: HTTP_INTERCEPTORS, useClass: TokenInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true}],
    
  bootstrap: [AppComponent]
})
export class AppModule { }
