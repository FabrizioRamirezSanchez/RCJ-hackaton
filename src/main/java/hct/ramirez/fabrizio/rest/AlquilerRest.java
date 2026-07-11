package hct.ramirez.fabrizio.rest;

import hct.ramirez.fabrizio.model.AlquilerModel;
import hct.ramirez.fabrizio.service.AlquilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/alquileres")
public class AlquilerRest {
    @Autowired
    private AlquilerService service;

    @GetMapping
    public Flux<AlquilerModel> listar() {
        return service.findAll();
    }

    @PostMapping
    public Mono<AlquilerModel> guardar(@RequestBody AlquilerModel alquiler) {
        return service.save(alquiler);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> eliminar(@PathVariable String id) {
        return service.deleteById(id);
    }
}