package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.BankStatementSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankStatementSummaryRepository  extends MongoRepository<BankStatementSummary,String> {

BankStatementSummary findByStatementId(String statementId);
}
