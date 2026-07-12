package hct.ramirez.fabrizio.rest;

import hct.ramirez.fabrizio.model.AlquilerModel;
import hct.ramirez.fabrizio.service.AlquilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/alquileres")
public class AlquilerRest {
    @Autowired
    private AlquilerService service;

    @GetMapping public Flux<AlquilerModel> listar() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<AlquilerModel> buscarPorId(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping public Mono<AlquilerModel> guardar(@RequestBody AlquilerModel alquiler) {
        if (alquiler.getId() != null && alquiler.getId().trim().isEmpty()) {
            alquiler.setId(null);
        }
        if (alquiler.getClienteId() == null || alquiler.getClienteId().trim().isEmpty()) {
            return Mono.error(new RuntimeException("El cliente es obligatorio"));
        }
        if (alquiler.getVehiculoId() == null || alquiler.getVehiculoId().trim().isEmpty()) {
            return Mono.error(new RuntimeException("El vehículo es obligatorio"));
        }
        if (alquiler.getDias() == null || alquiler.getDias() <= 0) {
            return Mono.error(new RuntimeException("Los días son obligatorios"));
        }
        if (alquiler.getFechaInicio() == null || alquiler.getFechaInicio().trim().isEmpty()) {
            return Mono.error(new RuntimeException("La fecha de inicio es obligatoria"));
        }
        if (alquiler.getFechaFin() == null || alquiler.getFechaFin().trim().isEmpty()) {
            return Mono.error(new RuntimeException("La fecha de fin es obligatoria"));
        }
        alquiler.setEstado("ACTIVO");
        return service.save(alquiler);
    }

    @PutMapping("/{id}")
    public Mono<AlquilerModel> editar(@PathVariable String id, @RequestBody AlquilerModel alquiler) {
        return service.update(id, alquiler);
    }

    @PatchMapping("/eliminar/{id}")
    public Mono<AlquilerModel> eliminarLogico(@PathVariable String id) {
        return service.cambiarEstado(id, "INACTIVO");
    }

    @PatchMapping("/restaurar/{id}")
    public Mono<AlquilerModel> restaurar(@PathVariable String id) {
        return service.cambiarEstado(id, "ACTIVO");
    }

    @DeleteMapping("/{id}") public Mono<Void> eliminar(@PathVariable String id) {
        return service.deleteById(id);
    }
}
