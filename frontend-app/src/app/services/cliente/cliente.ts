import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cliente } from './cliente.model';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  private apiUrl = 'http://hct-cliente-ramirez-fabrizio-service.hct-cliente-ramirez-fabrizio.svc.cluster.local:8080/api/clientes';

  constructor(private http: HttpClient) { }

  listar(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(this.apiUrl);
  }

  buscarPorId(id: string): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.apiUrl}/${id}`);
  }

  guardar(cliente: Cliente): Observable<Cliente> {
    return this.http.post<Cliente>(this.apiUrl, cliente);
  }

  editar(id: string, cliente: Cliente): Observable<Cliente> {
    return this.http.put<Cliente>(`${this.apiUrl}/${id}`, cliente);
  }

  eliminarLogico(id: string): Observable<Cliente> {
    return this.http.patch<Cliente>(`${this.apiUrl}/eliminar/${id}`, {});
  }

  restaurar(id: string): Observable<Cliente> {
    return this.http.patch<Cliente>(`${this.apiUrl}/restaurar/${id}`, {});
  }

  eliminarFisico(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}