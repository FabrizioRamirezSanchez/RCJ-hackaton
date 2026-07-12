export interface Alquiler {
  id?: string;
  clienteId: string;
  vehiculoId: string;
  dias: number;
  fechaInicio: string;
  fechaFin: string;
  total: number;
  estado: string;
}