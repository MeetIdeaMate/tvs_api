package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.StatementConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatementConfigRepository  extends MongoRepository<StatementConfig,String> {


    StatementConfig findByStatementConfigId(String statementConfigId);
}
