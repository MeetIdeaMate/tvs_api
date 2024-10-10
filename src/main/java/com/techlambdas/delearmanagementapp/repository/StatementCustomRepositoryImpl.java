package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class StatementCustomRepositoryImpl implements StatementCustomRepo {
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public Boolean existStatementByDate(LocalDate fromDate, LocalDate toDate) {
      Query query = new Query();
        query.addCriteria(Criteria.where("toDate").lt(fromDate)
                .orOperator(Criteria.where("fromDate").gt(toDate)));

        return mongoTemplate.exists(query, Statement.class);
    }
}
