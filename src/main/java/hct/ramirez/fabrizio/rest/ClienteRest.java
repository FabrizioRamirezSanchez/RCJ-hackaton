package hct.ramirez.fabrizio.rest;

import hct.ramirez.fabrizio.model.ClienteModel;
import hct.ramirez.fabrizio.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/clientes")
public class ClienteRest {
    @Autowired
    private ClienteService service;

    @GetMapping public Flux<ClienteModel> listar() {
        return service.findAll();
    }

    @PostMapping public Mono<ClienteModel> guardar(@RequestBody ClienteModel cliente) {
        return service.save(cliente);
    }

    @DeleteMapping("/{id}") public Mono<Void> eliminar(@PathVariable String id) {
        return service.deleteById(id);
    }
}