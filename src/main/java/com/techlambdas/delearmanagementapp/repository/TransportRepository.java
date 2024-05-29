package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Transport;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransportRepository extends MongoRepository<Transport, String> {
    Transport findBytransportId(String transportId);
}
