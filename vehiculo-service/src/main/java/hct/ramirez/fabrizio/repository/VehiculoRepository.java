package hct.ramirez.fabrizio.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import hct.ramirez.fabrizio.model.VehiculoModel;

@Repository
public interface VehiculoRepository extends ReactiveMongoRepository<VehiculoModel, String> {
    
}
