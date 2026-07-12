package hct.ramirez.fabrizio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import hct.ramirez.fabrizio.model.AlquilerModel;
import hct.ramirez.fabrizio.repository.AlquilerRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AlquilerService {
    @Autowired
    private AlquilerRepository alquilerRepository;
    
    private final WebClient webClient;

    public AlquilerService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Flux<AlquilerModel> findAll() {
        return alquilerRepository.findAll();
    }

    public Mono<AlquilerModel> findById(String id) {
        return alquilerRepository.findById(id);
    }

    public Mono<AlquilerModel> save(AlquilerModel alquiler) {
        // Verificar que el vehículo esté disponible
        return webClient.get()
                .uri("http://hct-vehiculo-ramirez-fabrizio-service.hct-vehiculo-ramirez-fabrizio.svc.cluster.local:8080/api/vehiculos/" + alquiler.getVehiculoId())
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(vehiculoJson -> {
                    if (vehiculoJson.contains("\"estado\":\"DISPONIBLE\"")) {
                        // Verificar que el cliente esté activo
                        return webClient.get()
                                .uri("http://hct-cliente-ramirez-fabrizio-service.hct-cliente-ramirez-fabrizio.svc.cluster.local:8080/api/clientes/" + alquiler.getClienteId())
                                .retrieve()
                                .bodyToMono(String.class)
                                .flatMap(clienteJson -> {
                                    if (clienteJson.contains("\"estado\":\"ACTIVO\"")) {
                                        return alquilerRepository.save(alquiler)
                                                .flatMap(savedAlquiler -> {
                                                    // Actualizar estado del vehículo a ALQUILADO
                                                    return webClient.patch()
                                                            .uri("http://hct-vehiculo-ramirez-fabrizio-service.hct-vehiculo-ramirez-fabrizio.svc.cluster.local:8080/api/vehiculos/alquilar/" + alquiler.getVehiculoId())
                                                            .retrieve()
                                                            .bodyToMono(Void.class)
                                                            .thenReturn(savedAlquiler)
                                                            .onErrorResume(e -> Mono.just(savedAlquiler));
                                                });
                                    } else {
                                        return Mono.error(new RuntimeException("El cliente no está activo"));
                                    }
                                })
                                .onErrorResume(e -> Mono.error(new RuntimeException("Cliente no encontrado")));
                    } else {
                        return Mono.error(new RuntimeException("El vehículo no está disponible"));
                    }
                })
                .onErrorResume(e -> Mono.error(new RuntimeException("Vehículo no encontrado")));
    }

    public Mono<AlquilerModel> update(String id, AlquilerModel alquiler) {
        return alquilerRepository.findById(id)
                .flatMap(existingAlquiler -> {
                    // Verificar que el vehículo esté disponible o sea el mismo
                    return webClient.get()
                            .uri("http://hct-vehiculo-ramirez-fabrizio-service.hct-vehiculo-ramirez-fabrizio.svc.cluster.local:8080/api/vehiculos/" + alquiler.getVehiculoId())
                            .retrieve()
                            .bodyToMono(String.class)
                            .flatMap(vehiculoJson -> {
                                // Verificar que el vehículo esté activo
                                if (!vehiculoJson.contains("\"estado\":\"DISPONIBLE\"") && !vehiculoJson.contains("\"estado\":\"ALQUILADO\"")) {
                                    return Mono.error(new RuntimeException("El vehículo no está disponible"));
                                }
                                
                                // Verificar que el cliente esté activo
                                return webClient.get()
                                        .uri("http://hct-cliente-ramirez-fabrizio-service.hct-cliente-ramirez-fabrizio.svc.cluster.local:8080/api/clientes/" + alquiler.getClienteId())
                                        .retrieve()
                                        .bodyToMono(String.class)
                                        .flatMap(clienteJson -> {
                                            if (!clienteJson.contains("\"estado\":\"ACTIVO\"")) {
                                                return Mono.error(new RuntimeException("El cliente no está activo"));
                                            }
                                            
                                            alquiler.setId(id);
                                            String estadoAnterior = existingAlquiler.getEstado();
                                            String estadoNuevo = alquiler.getEstado();
                                            
                                            return alquilerRepository.save(alquiler)
                                                    .flatMap(savedAlquiler -> {
                                                        // Si el estado cambia a ACTIVO, alquilar el vehículo
                                                        if ("ACTIVO".equals(estadoNuevo) && !"ACTIVO".equals(estadoAnterior)) {
                                                            return webClient.patch()
                                                                    .uri("http://hct-vehiculo-ramirez-fabrizio-service.hct-vehiculo-ramirez-fabrizio.svc.cluster.local:8080/api/vehiculos/alquilar/" + alquiler.getVehiculoId())
                                                                    .retrieve()
                                                                    .bodyToMono(Void.class)
                                                                    .thenReturn(savedAlquiler)
                                                                    .onErrorResume(e -> Mono.just(savedAlquiler));
                                                        }
                                                        // Si el estado cambia de ACTIVO a otro, liberar el vehículo
                                                        else if (!"ACTIVO".equals(estadoNuevo) && "ACTIVO".equals(estadoAnterior)) {
                                                            return webClient.patch()
                                                                    .uri("http://hct-vehiculo-ramirez-fabrizio-service.hct-vehiculo-ramirez-fabrizio.svc.cluster.local:8080/api/vehiculos/liberar/" + alquiler.getVehiculoId())
                                                                    .retrieve()
                                                                    .bodyToMono(Void.class)
                                                                    .thenReturn(savedAlquiler)
                                                                    .onErrorResume(e -> Mono.just(savedAlquiler));
                                                        }
                                                        return Mono.just(savedAlquiler);
                                                    });
                                        })
                                        .onErrorResume(e -> Mono.error(new RuntimeException("Cliente no encontrado")));
                            })
                            .onErrorResume(e -> Mono.error(new RuntimeException("Vehículo no encontrado")));
                });
    }

    public Mono<Void> deleteById(String id) {
        return alquilerRepository.findById(id)
                .flatMap(alquiler -> {
                    // Cambiar estado a CANCELADO y liberar el vehículo
                    alquiler.setEstado("CANCELADO");
                    return alquilerRepository.save(alquiler)
                            .flatMap(savedAlquiler -> {
                                // Restaurar estado del vehículo a DISPONIBLE
                                return webClient.patch()
                                        .uri("http://hct-vehiculo-ramirez-fabrizio-service.hct-vehiculo-ramirez-fabrizio.svc.cluster.local:8080/api/vehiculos/liberar/" + alquiler.getVehiculoId())
                                        .retrieve()
                                        .bodyToMono(Void.class)
                                        .then()
                                        .onErrorResume(e -> Mono.empty()); // Si falla, aún cambia el estado
                            });
                })
                .switchIfEmpty(Mono.empty());
    }

    public Mono<AlquilerModel> cambiarEstado(String id, String nuevoEstado) {
        return alquilerRepository.findById(id)
                .flatMap(alquiler -> {
                    alquiler.setEstado(nuevoEstado);
                    return alquilerRepository.save(alquiler)
                            .flatMap(savedAlquiler -> {
                                // Si el estado es INACTIVO, liberar el vehículo
                                if ("INACTIVO".equals(nuevoEstado)) {
                                    return webClient.patch()
                                            .uri("http://hct-vehiculo-ramirez-fabrizio-service.hct-vehiculo-ramirez-fabrizio.svc.cluster.local:8080/api/vehiculos/liberar/" + alquiler.getVehiculoId())
                                            .retrieve()
                                            .bodyToMono(Void.class)
                                            .thenReturn(savedAlquiler)
                                            .onErrorResume(e -> Mono.just(savedAlquiler));
                                }
                                return Mono.just(savedAlquiler);
                            });
                });
    }
}
