package hct.ramirez.fabrizio.service;

import hct.ramirez.fabrizio.model.AlquilerModel;
import hct.ramirez.fabrizio.model.ClienteModel;
import hct.ramirez.fabrizio.model.VehiculoModel;
import hct.ramirez.fabrizio.repository.AlquilerRepository;
import hct.ramirez.fabrizio.repository.ClienteRepository;
import hct.ramirez.fabrizio.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AlquilerService {
    @Autowired
    private AlquilerRepository alquilerRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private VehiculoRepository vehiculoRepository;

    public Flux<AlquilerModel> findAll() {
        return alquilerRepository.findAll();
    }

    public Mono<AlquilerModel> findById(String id) {
        return alquilerRepository.findById(id);
    }

    public Mono<AlquilerModel> save(AlquilerModel alquiler) {
        // Validar que el cliente exista
        return clienteRepository.findById(alquiler.getClienteId())
            .switchIfEmpty(Mono.error(new RuntimeException("El cliente no existe")))
            .flatMap(cliente -> {
                // Validar que el vehículo exista y esté disponible
                return vehiculoRepository.findById(alquiler.getVehiculoId())
                    .switchIfEmpty(Mono.error(new RuntimeException("El vehículo no existe")))
                    .flatMap(vehiculo -> {
                        if (!"DISPONIBLE".equals(vehiculo.getEstado())) {
                            return Mono.error(new RuntimeException("El vehículo no está disponible"));
                        }
                        // Cambiar estado del vehículo a ALQUILADO
                        vehiculo.setEstado("ALQUILADO");
                        return vehiculoRepository.save(vehiculo)
                            .flatMap(vehiculoActualizado -> {
                                // Guardar el alquiler
                                return alquilerRepository.save(alquiler);
                            });
                    });
            });
    }

    public Mono<AlquilerModel> update(String id, AlquilerModel alquiler) {
        return alquilerRepository.findById(id)
                .flatMap(existingAlquiler -> {
                    alquiler.setId(id);
                    return alquilerRepository.save(alquiler);
                });
    }

    public Mono<Void> deleteById(String id) {
        return alquilerRepository.findById(id)
            .flatMap(alquiler -> {
                // Liberar el vehículo al eliminar el alquiler
                return vehiculoRepository.findById(alquiler.getVehiculoId())
                    .flatMap(vehiculo -> {
                        vehiculo.setEstado("DISPONIBLE");
                        return vehiculoRepository.save(vehiculo)
                            .flatMap(vehiculoActualizado -> {
                                return alquilerRepository.deleteById(id);
                            });
                    });
            })
            .switchIfEmpty(alquilerRepository.deleteById(id));
    }

    public Mono<AlquilerModel> cambiarEstado(String id, String nuevoEstado) {
        return alquilerRepository.findById(id)
                .flatMap(alquiler -> {
                    String estadoAnterior = alquiler.getEstado();
                    alquiler.setEstado(nuevoEstado);
                    return alquilerRepository.save(alquiler)
                        .flatMap(alquilerActualizado -> {
                            // Si se cancela o completa el alquiler, liberar el vehículo
                            if (("CANCELADO".equals(nuevoEstado) || "COMPLETADO".equals(nuevoEstado)) && 
                                ("PENDIENTE".equals(estadoAnterior) || "ACTIVO".equals(estadoAnterior))) {
                                return vehiculoRepository.findById(alquiler.getVehiculoId())
                                    .flatMap(vehiculo -> {
                                        vehiculo.setEstado("DISPONIBLE");
                                        return vehiculoRepository.save(vehiculo)
                                            .thenReturn(alquilerActualizado);
                                    });
                            }
                            return Mono.just(alquilerActualizado);
                        });
                });
    }
}