package hct.ramirez.fabrizio.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import hct.ramirez.fabrizio.model.AlquilerModel;

@Repository
public interface AlquilerRepository extends ReactiveMongoRepository<AlquilerModel, String> {
    
}
