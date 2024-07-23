package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.AccessControl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessControlRepository extends MongoRepository<AccessControl, String> {

}
