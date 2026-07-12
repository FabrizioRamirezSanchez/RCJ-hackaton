import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { VehiculoService } from './vehiculo';
import { Vehiculo } from './vehiculo.model';

@Component({
  selector: 'app-vehiculo-list',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './vehiculo-list.html',
  styleUrls: ['./vehiculo-list.css']
})
export class VehiculoList implements OnInit {
  vehiculos: Vehiculo[] = [];
  vehiculoForm: FormGroup;
  editando = false;
  idEditando: string | null | undefined = null;
  mostrarFormulario = false;

  constructor(private service: VehiculoService, private fb: FormBuilder) {
    this.vehiculoForm = this.fb.group({
      id: [''],
      placa: ['', Validators.required],
      marca: ['', Validators.required],
      modelo: ['', Validators.required],
      anio: ['', Validators.required],
      color: ['', Validators.required],
      precioPorDia: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.cargarVehiculos();
  }

  cargarVehiculos(): void {
    this.service.listar().subscribe({
      next: (data: Vehiculo[]) => this.vehiculos = data,
      error: (err) => console.error('Error:', err)
    });
  }

  mostrarCrear(): void {
    this.editando = false;
    this.idEditando = null;
    this.mostrarFormulario = true;
    this.vehiculoForm.reset();
  }

  mostrarEditar(vehiculo: Vehiculo): void {
    this.editando = true;
    this.idEditando = vehiculo.id;
    this.mostrarFormulario = true;
    this.vehiculoForm.patchValue(vehiculo);
  }

  cancelar(): void {
    this.mostrarFormulario = false;
    this.vehiculoForm.reset();
  }

  guardar(): void {
    if (this.vehiculoForm.invalid) return;

    const formValue = this.vehiculoForm.value;
    const vehiculo: Vehiculo = {
      placa: formValue.placa,
      marca: formValue.marca,
      modelo: formValue.modelo,
      anio: Number(formValue.anio),
      color: formValue.color,
      precioPorDia: Number(formValue.precioPorDia),
      estado: 'DISPONIBLE'
    };

    if (this.editando && this.idEditando) {
      vehiculo.id = this.idEditando;
      this.service.editar(this.idEditando, vehiculo).subscribe({
        next: () => {
          this.cargarVehiculos();
          this.cancelar();
        },
        error: (err) => console.error('Error:', err)
      });
    } else {
      this.service.guardar(vehiculo).subscribe({
        next: () => {
          this.cargarVehiculos();
          this.cancelar();
        },
        error: (err) => console.error('Error:', err)
      });
    }
  }

  eliminarLogico(id: string | undefined): void {
    if (!id) return;
    this.service.eliminarLogico(id).subscribe({
      next: () => this.cargarVehiculos(),
      error: (err) => console.error('Error:', err)
    });
  }

  restaurar(id: string | undefined): void {
    if (!id) return;
    this.service.restaurar(id).subscribe({
      next: () => this.cargarVehiculos(),
      error: (err) => console.error('Error:', err)
    });
  }
}