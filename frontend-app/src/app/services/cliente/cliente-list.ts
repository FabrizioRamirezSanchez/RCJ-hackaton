import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClienteService } from './cliente';
import { Cliente } from './cliente.model';

@Component({
  selector: 'app-cliente-list',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './cliente-list.html',
  styleUrls: ['./cliente-list.css']
})
export class ClienteList implements OnInit {
  clientes: Cliente[] = [];
  clienteForm: FormGroup;
  editando = false;
  idEditando: string | null | undefined = null;
  mostrarFormulario = false;

  constructor(private service: ClienteService, private fb: FormBuilder) {
    this.clienteForm = this.fb.group({
      id: [''],
      dni: ['', Validators.required],
      nombres: ['', Validators.required],
      apellidos: ['', Validators.required],
      celular: ['', Validators.required],
      correo: ['', [Validators.required, Validators.email]],
      licencia: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.cargarClientes();
  }

  cargarClientes(): void {
    this.service.listar().subscribe({
      next: (data: Cliente[]) => this.clientes = data,
      error: (err) => console.error('Error cargando clientes:', err)
    });
  }

  mostrarCrear(): void {
    this.editando = false;
    this.idEditando = null;
    this.mostrarFormulario = true;
    this.clienteForm.reset();
  }

  mostrarEditar(cliente: Cliente): void {
    this.editando = true;
    this.idEditando = cliente.id;
    this.mostrarFormulario = true;
    this.clienteForm.patchValue(cliente);
  }

  cancelar(): void {
    this.mostrarFormulario = false;
    this.clienteForm.reset();
  }

  guardar(): void {
    if (this.clienteForm.invalid) return;

    const formValue = this.clienteForm.value;
    const cliente: Cliente = {
      dni: formValue.dni,
      nombres: formValue.nombres,
      apellidos: formValue.apellidos,
      celular: formValue.celular,
      correo: formValue.correo,
      licencia: formValue.licencia,
      estado: 'ACTIVO'
    };

    if (this.editando && this.idEditando) {
      cliente.id = this.idEditando;
      this.service.editar(this.idEditando, cliente).subscribe({
        next: () => {
          this.cargarClientes();
          this.cancelar();
        },
        error: (err) => console.error('Error:', err)
      });
    } else {
      this.service.guardar(cliente).subscribe({
        next: () => {
          this.cargarClientes();
          this.cancelar();
        },
        error: (err) => console.error('Error:', err)
      });
    }
  }

  eliminarLogico(id: string | undefined): void {
    if (!id) return;
    this.service.eliminarLogico(id).subscribe({
      next: () => this.cargarClientes(),
      error: (err) => console.error('Error:', err)
    });
  }

  restaurar(id: string | undefined): void {
    if (!id) return;
    this.service.restaurar(id).subscribe({
      next: () => this.cargarClientes(),
      error: (err) => console.error('Error:', err)
    });
  }
}