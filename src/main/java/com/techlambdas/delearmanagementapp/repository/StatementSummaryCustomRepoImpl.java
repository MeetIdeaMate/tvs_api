package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.model.BankStatementSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class StatementSummaryCustomRepoImpl implements StatementSummaryCustomRepo {

    @Autowired
    MongoTemplate mongoTemplate;
    @Override
    public BankStatementSummary getStatementSummary(AccountType accountType, String accountHeadName, String statementId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("statementId").is(statementId));
        if(accountType !=null)
          query.addCriteria(Criteria.where("bankStatementSummaryList.accountType").is(accountType));
        if(accountHeadName !=null)
          query.addCriteria(Criteria.where("bankStatementSummaryList.accountHeadName").is(accountHeadName));
        return mongoTemplate.findOne(query, BankStatementSummary.class);
    }
}
