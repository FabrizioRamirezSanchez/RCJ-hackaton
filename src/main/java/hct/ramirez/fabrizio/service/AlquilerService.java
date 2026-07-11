package hct.ramirez.fabrizio.service;

import hct.ramirez.fabrizio.model.AlquilerModel;
import hct.ramirez.fabrizio.repository.AlquilerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AlquilerService {
    @Autowired
    private AlquilerRepository alquilerRepository;

    public Flux<AlquilerModel> findAll() {
        return alquilerRepository.findAll();
    }

    public Mono<AlquilerModel> save(AlquilerModel alquiler) {
        return alquilerRepository.save(alquiler);
    }

    public Mono<Void> deleteById(String id) {
        return alquilerRepository.deleteById(id);
    }
}