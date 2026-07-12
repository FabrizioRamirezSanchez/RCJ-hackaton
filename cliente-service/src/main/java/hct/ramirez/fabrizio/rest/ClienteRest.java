package hct.ramirez.fabrizio.rest;

import hct.ramirez.fabrizio.model.ClienteModel;
import hct.ramirez.fabrizio.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/clientes")
public class ClienteRest {
    @Autowired
    private ClienteService service;

    @GetMapping public Flux<ClienteModel> listar() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ClienteModel> buscarPorId(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping public Mono<ClienteModel> guardar(@RequestBody ClienteModel cliente) {
        if (cliente.getId() != null && cliente.getId().trim().isEmpty()) {
            cliente.setId(null);
        }
        if (cliente.getDni() == null || cliente.getDni().trim().isEmpty()) {
            return Mono.error(new RuntimeException("El DNI es obligatorio"));
        }
        if (cliente.getNombres() == null || cliente.getNombres().trim().isEmpty()) {
            return Mono.error(new RuntimeException("Los nombres son obligatorios"));
        }
        if (cliente.getApellidos() == null || cliente.getApellidos().trim().isEmpty()) {
            return Mono.error(new RuntimeException("Los apellidos son obligatorios"));
        }
        if (cliente.getCelular() == null || cliente.getCelular().trim().isEmpty()) {
            return Mono.error(new RuntimeException("El celular es obligatorio"));
        }
        if (cliente.getCorreo() == null || cliente.getCorreo().trim().isEmpty()) {
            return Mono.error(new RuntimeException("El correo es obligatorio"));
        }
        if (cliente.getLicencia() == null || cliente.getLicencia().trim().isEmpty()) {
            return Mono.error(new RuntimeException("La licencia es obligatoria"));
        }
        cliente.setEstado("ACTIVO");
        return service.save(cliente);
    }

    @PutMapping("/{id}")
    public Mono<ClienteModel> editar(@PathVariable String id, @RequestBody ClienteModel cliente) {
        return service.update(id, cliente);
    }

    @PatchMapping("/eliminar/{id}")
    public Mono<ClienteModel> eliminarLogico(@PathVariable String id) {
        return service.cambiarEstado(id, "INACTIVO");
    }

    @PatchMapping("/restaurar/{id}")
    public Mono<ClienteModel> restaurar(@PathVariable String id) {
        return service.cambiarEstado(id, "ACTIVO");
    }

    @DeleteMapping("/{id}") public Mono<Void> eliminar(@PathVariable String id) {
        return service.deleteById(id);
    }
}
