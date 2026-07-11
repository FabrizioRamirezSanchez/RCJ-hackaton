package hct.ramirez.fabrizio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hct.ramirez.fabrizio.model.VehiculoModel;
import hct.ramirez.fabrizio.repository.VehiculoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VehiculoService {
    @Autowired
    private VehiculoRepository vehiculoRepository;

    public Flux<VehiculoModel> findAll() {
        return vehiculoRepository.findAll();
    }

    public Mono<VehiculoModel> save(VehiculoModel vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }

    public Mono<Void> deleteById(String id) {
        return vehiculoRepository.deleteById(id);
    }
}
