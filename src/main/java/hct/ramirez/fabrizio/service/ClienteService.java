package hct.ramirez.fabrizio.service;

import hct.ramirez.fabrizio.model.ClienteModel;
import hct.ramirez.fabrizio.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    public Flux<ClienteModel> findAll() { 
        return clienteRepository.findAll();
    }

    public Mono<ClienteModel> save(ClienteModel cliente) { 
        return clienteRepository.save(cliente);
    }

    public Mono<Void> deleteById(String id) { 
        return clienteRepository.deleteById(id); 
    }
}