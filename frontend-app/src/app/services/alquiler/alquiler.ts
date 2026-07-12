import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Alquiler } from './alquiler.model';

@Injectable({
  providedIn: 'root'
})
export class AlquilerService {
  private apiUrl = 'http://hct-alquiler-ramirez-fabrizio-service.hct-alquiler-ramirez-fabrizio.svc.cluster.local:8080/api/alquileres';

  constructor(private http: HttpClient) { }

  listar(): Observable<Alquiler[]> {
    return this.http.get<Alquiler[]>(this.apiUrl);
  }

  buscarPorId(id: string): Observable<Alquiler> {
    return this.http.get<Alquiler>(`${this.apiUrl}/${id}`);
  }

  guardar(alquiler: Alquiler): Observable<Alquiler> {
    return this.http.post<Alquiler>(this.apiUrl, alquiler);
  }

  editar(id: string, alquiler: Alquiler): Observable<Alquiler> {
    return this.http.put<Alquiler>(`${this.apiUrl}/${id}`, alquiler);
  }

  eliminarLogico(id: string): Observable<Alquiler> {
    return this.http.patch<Alquiler>(`${this.apiUrl}/eliminar/${id}`, {});
  }

  restaurar(id: string): Observable<Alquiler> {
    return this.http.patch<Alquiler>(`${this.apiUrl}/restaurar/${id}`, {});
  }

  eliminarFisico(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}