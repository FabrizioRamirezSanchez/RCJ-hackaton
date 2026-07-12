import { Routes } from '@angular/router';
import { VehiculoList } from './services/vehiculo/vehiculo-list';
import { ClienteList } from './services/cliente/cliente-list';
import { AlquilerList } from './services/alquiler/alquiler-list';

export const routes: Routes = [
  { path: 'vehiculos', component: VehiculoList },
  { path: 'clientes', component: ClienteList },
  { path: 'alquileres', component: AlquilerList },
  { path: '', redirectTo: 'vehiculos', pathMatch: 'full' }
];