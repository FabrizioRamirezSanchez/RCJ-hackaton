package hct.ramirez.fabrizio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hct.ramirez.fabrizio.model.ClienteModel;
import hct.ramirez.fabrizio.repository.ClienteRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    public Flux<ClienteModel> findAll() {
        return clienteRepository.findAll();
    }

    public Mono<ClienteModel> findById(String id) {
        return clienteRepository.findById(id);
    }

    public Mono<ClienteModel> save(ClienteModel cliente) {
        return clienteRepository.save(cliente);
    }

    public Mono<ClienteModel> update(String id, ClienteModel cliente) {
        return clienteRepository.findById(id)
                .flatMap(existingCliente -> {
                    cliente.setId(id);
                    return clienteRepository.save(cliente);
                });
    }

    public Mono<Void> deleteById(String id) {
        return clienteRepository.deleteById(id);
    }

    public Mono<ClienteModel> cambiarEstado(String id, String nuevoEstado) {
        return clienteRepository.findById(id)
                .flatMap(cliente -> {
                    cliente.setEstado(nuevoEstado);
                    return clienteRepository.save(cliente);
                });
    }
}
