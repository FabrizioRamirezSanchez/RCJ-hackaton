import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Vehiculo } from './vehiculo.model';

@Injectable({
  providedIn: 'root'
})
export class VehiculoService {
  private apiUrl = 'http://hct-vehiculo-ramirez-fabrizio-service.hct-vehiculo-ramirez-fabrizio.svc.cluster.local:8080/api/vehiculos';

  constructor(private http: HttpClient) { }

  listar(): Observable<Vehiculo[]> {
    return this.http.get<Vehiculo[]>(this.apiUrl);
  }

  buscarPorId(id: string): Observable<Vehiculo> {
    return this.http.get<Vehiculo>(`${this.apiUrl}/${id}`);
  }

  guardar(vehiculo: Vehiculo): Observable<Vehiculo> {
    return this.http.post<Vehiculo>(this.apiUrl, vehiculo);
  }

  editar(id: string, vehiculo: Vehiculo): Observable<Vehiculo> {
    return this.http.put<Vehiculo>(`${this.apiUrl}/${id}`, vehiculo);
  }

  eliminarLogico(id: string): Observable<Vehiculo> {
    return this.http.patch<Vehiculo>(`${this.apiUrl}/eliminar/${id}`, {});
  }

  restaurar(id: string): Observable<Vehiculo> {
    return this.http.patch<Vehiculo>(`${this.apiUrl}/restaurar/${id}`, {});
  }

  eliminarFisico(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}