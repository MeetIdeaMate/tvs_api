package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Config;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends MongoRepository<Config,String> {
    Config findConfigByConfigId(String configId);
}
