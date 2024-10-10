package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Statement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface StatementRepository extends MongoRepository<Statement,String> {


    Statement findByStatementId(String statementId);

    Statement findByStatementDate(String statementDate);


}
