package hct.ramirez.fabrizio.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import hct.ramirez.fabrizio.model.ClienteModel;

@Repository
public interface ClienteRepository extends ReactiveMongoRepository<ClienteModel, String> {
    
}
