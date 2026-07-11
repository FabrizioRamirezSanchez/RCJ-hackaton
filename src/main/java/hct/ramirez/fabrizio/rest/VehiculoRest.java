package hct.ramirez.fabrizio.rest;

import hct.ramirez.fabrizio.model.VehiculoModel;
import hct.ramirez.fabrizio.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoRest {
    @Autowired
    private VehiculoService service;

    @GetMapping public Flux<VehiculoModel> listar() {
        return service.findAll();
    }

    @PostMapping public Mono<VehiculoModel> guardar(@RequestBody VehiculoModel vehiculo) {
        return service.save(vehiculo);
    }

    @DeleteMapping("/{id}") public Mono<Void> eliminar(@PathVariable String id) {
        return service.deleteById(id);
    }
}