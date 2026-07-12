import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AlquilerService } from './alquiler';
import { Alquiler } from './alquiler.model';

@Component({
  selector: 'app-alquiler-list',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './alquiler-list.html',
  styleUrls: ['./alquiler-list.css']
})
export class AlquilerList implements OnInit {
  alquileres: Alquiler[] = [];
  alquilerForm: FormGroup;
  editando = false;
  idEditando: string | null | undefined = null;
  mostrarFormulario = false;

  constructor(private service: AlquilerService, private fb: FormBuilder) {
    this.alquilerForm = this.fb.group({
      id: [''],
      clienteId: ['', Validators.required],
      vehiculoId: ['', Validators.required],
      dias: ['', Validators.required],
      fechaInicio: ['', Validators.required],
      fechaFin: ['', Validators.required],
      total: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.cargarAlquileres();
  }

  cargarAlquileres(): void {
    this.service.listar().subscribe({
      next: (data: Alquiler[]) => this.alquileres = data,
      error: (err) => console.error('Error cargando alquileres:', err)
    });
  }

  mostrarCrear(): void {
    this.editando = false;
    this.idEditando = null;
    this.mostrarFormulario = true;
    this.alquilerForm.reset();
  }

  mostrarEditar(alquiler: Alquiler): void {
    this.editando = true;
    this.idEditando = alquiler.id;
    this.mostrarFormulario = true;
    this.alquilerForm.patchValue(alquiler);
  }

  cancelar(): void {
    this.mostrarFormulario = false;
    this.alquilerForm.reset();
  }

  guardar(): void {
    if (this.alquilerForm.invalid) return;

    const formValue = this.alquilerForm.value;
    const alquiler: Alquiler = {
      clienteId: formValue.clienteId,
      vehiculoId: formValue.vehiculoId,
      dias: Number(formValue.dias),
      fechaInicio: formValue.fechaInicio,
      fechaFin: formValue.fechaFin,
      total: Number(formValue.total),
      estado: 'ACTIVO'
    };

    if (this.editando && this.idEditando) {
      alquiler.id = this.idEditando;
      this.service.editar(this.idEditando, alquiler).subscribe({
        next: () => {
          this.cargarAlquileres();
          this.cancelar();
        },
        error: (err) => console.error('Error:', err)
      });
    } else {
      this.service.guardar(alquiler).subscribe({
        next: () => {
          this.cargarAlquileres();
          this.cancelar();
        },
        error: (err) => console.error('Error:', err)
      });
    }
  }

  eliminarLogico(id: string | undefined): void {
    if (!id) return;
    this.service.eliminarLogico(id).subscribe({
      next: () => this.cargarAlquileres(),
      error: (err) => console.error('Error:', err)
    });
  }

  restaurar(id: string | undefined): void {
    if (!id) return;
    this.service.restaurar(id).subscribe({
      next: () => this.cargarAlquileres(),
      error: (err) => console.error('Error:', err)
    });
  }
}