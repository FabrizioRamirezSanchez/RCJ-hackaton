package hct.ramirez.fabrizio.rest;

import hct.ramirez.fabrizio.model.VehiculoModel;
import hct.ramirez.fabrizio.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoRest {
    @Autowired
    private VehiculoService service;

    @GetMapping public Flux<VehiculoModel> listar() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<VehiculoModel> buscarPorId(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping public Mono<VehiculoModel> guardar(@RequestBody VehiculoModel vehiculo) {
        if (vehiculo.getId() != null && vehiculo.getId().trim().isEmpty()) {
            vehiculo.setId(null);
        }
        if (vehiculo.getPlaca() == null || vehiculo.getPlaca().trim().isEmpty()) {
            return Mono.error(new RuntimeException("La placa es obligatoria"));
        }
        if (vehiculo.getMarca() == null || vehiculo.getMarca().trim().isEmpty()) {
            return Mono.error(new RuntimeException("La marca es obligatoria"));
        }
        if (vehiculo.getModelo() == null || vehiculo.getModelo().trim().isEmpty()) {
            return Mono.error(new RuntimeException("El modelo es obligatorio"));
        }
        if (vehiculo.getAnio() == null) {
            return Mono.error(new RuntimeException("El año es obligatorio"));
        }
        if (vehiculo.getColor() == null || vehiculo.getColor().trim().isEmpty()) {
            return Mono.error(new RuntimeException("El color es obligatorio"));
        }
        if (vehiculo.getPrecioPorDia() == null) {
            return Mono.error(new RuntimeException("El precio por día es obligatorio"));
        }
        vehiculo.setEstado("DISPONIBLE");
        return service.save(vehiculo);
    }

    @PutMapping("/{id}")
    public Mono<VehiculoModel> editar(@PathVariable String id, @RequestBody VehiculoModel vehiculo) {
        return service.update(id, vehiculo);
    }

    @PatchMapping("/eliminar/{id}")
    public Mono<VehiculoModel> eliminarLogico(@PathVariable String id) {
        return service.cambiarEstado(id, "INACTIVO");
    }

    @PatchMapping("/restaurar/{id}")
    public Mono<VehiculoModel> restaurar(@PathVariable String id) {
        return service.cambiarEstado(id, "DISPONIBLE");
    }

    @PatchMapping("/alquilar/{id}")
    public Mono<VehiculoModel> alquilar(@PathVariable String id) {
        return service.alquilar(id);
    }

    @PatchMapping("/liberar/{id}")
    public Mono<VehiculoModel> liberar(@PathVariable String id) {
        return service.liberar(id);
    }

    @DeleteMapping("/{id}") public Mono<Void> eliminar(@PathVariable String id) {
        return service.deleteById(id);
    }
}
