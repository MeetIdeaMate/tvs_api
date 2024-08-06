package com.techlambdas.delearmanagementapp.repository;


import com.mongodb.BasicDBObject;
import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.model.Account;
import com.techlambdas.delearmanagementapp.response.AccountDataSummary;
import com.techlambdas.delearmanagementapp.response.Balance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class AccountCustomRepoImpl implements AccountCustomRepo{

    private final MongoTemplate mongoTemplate;

    @Autowired
    public AccountCustomRepoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Balance> filterAcc(LocalDate transactDate) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("transactDate").lt(Date.from(transactDate.atStartOfDay().toInstant(ZoneOffset.ofHours(0))))
                        .and("cancelled").is(false)),
                group("transactType").sum("amount").as("totalAmount"),
                group("$_id.balanceAmount")
                        .sum(ConditionalOperators
                                .when(Criteria.where("_id").is("CREDIT"))
                                .thenValueOf("totalAmount")
                                .otherwiseValueOf(ArithmeticOperators.Multiply.valueOf("totalAmount").multiplyBy(-1)))
                        .as("balanceAmount"),
                project ("balanceAmount")
        );

        AggregationResults<Balance> accounts = mongoTemplate.aggregate(aggregation, Account.class, Balance.class);
        List<Balance> accountResult = accounts.getMappedResults();
        return accountResult;
    }

    @Override
    public List<Account> findByTransactDateBetween(LocalDate transacDate, LocalDate endDate) {
        Query query = new Query();
        query.addCriteria(Criteria.where("transactDate").gte(transacDate).lte(endDate));

        return mongoTemplate.find(query, Account.class);
    }

    @Override
    public Balance closingBalance(LocalDate transactDate) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("transactDate").gte(Date.from(transactDate.atStartOfDay().toInstant(ZoneOffset.ofHours(0))))
                        .and("cancelled").is(false)),
                group("transactType").sum("amount").as("totalAmount"),
                group("$_id.balanceAmount")
                        .sum(ConditionalOperators
                                .when(Criteria.where("_id").is("CREDIT"))
                                .thenValueOf("totalAmount")
                                .otherwiseValueOf(ArithmeticOperators.Multiply.valueOf("totalAmount").multiplyBy(-1)))
                        .as("closingBalance"),
                        project("closingBalance")
        );
         Balance accounts = mongoTemplate.aggregate(aggregation,Account.class, Balance.class).getUniqueMappedResult();
        return accounts;
    }


    public List<AccountDataSummary> getAggregateResultByOperator(String operatorCode) {

        Aggregation aggQuery = newAggregation(
                match(Criteria.where("createdBy").is(operatorCode)),
                group("createdBy","accountHeadCode","accountHeadName").count().as("receiptCount").sum("amount").as("amountCollected"),
                group("createdBy").push(new BasicDBObject
                        ("accountHeadCode","$_id.accountHeadCode").append
                        ("accountHeadName","$_id.accountHeadName").append
                        ( "receiptCount","$receiptCount").append
                        ("amountCollected","$amountCollected")
                ).as("summary"),
                project("summary").and("$_id").as("operatorCode")

        );
        AggregationResults<AccountDataSummary> accountSummary= mongoTemplate.aggregate(aggQuery, Account.class,AccountDataSummary.class);
        List<AccountDataSummary> accountDataSummary = accountSummary.getMappedResults();
        return accountDataSummary;
    }

    @Override
    public Page<Account> getByAccType(int page, int size, String financialYear, String accountHeadCode, String accountHeadName, String transactorId, AccountType transactType, String transactorName, String transactDesc, String shortNotes, String transactRefNo, String transactDetails, LocalDate transactDate,LocalDate fromDate,LocalDate toDate) {
        Pageable pageable = PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,"createdDateTime"));
        Query query = new Query();
        List<Criteria> criteria = new ArrayList<>();
        if(financialYear != null && !financialYear.isEmpty())
            criteria.add(Criteria.where("financialYear").is(financialYear));
        if(accountHeadCode != null && !accountHeadCode.isEmpty())
            criteria.add(Criteria.where("accountHeadCode").regex(accountHeadCode));
        if(accountHeadName != null && !accountHeadName.isEmpty())
            criteria.add(Criteria.where("accountHeadName").regex(accountHeadName));
        if(transactorId != null && !transactorId.isEmpty())
            criteria.add(Criteria.where("transactorId").is(transactorId));
        if( transactType != null) {
            if (transactType == AccountType.DEBIT)
                criteria.add(Criteria.where("transactType").is(AccountType.DEBIT));
            else if (transactType == AccountType.CREDIT)
                criteria.add(Criteria.where("transactType").is(AccountType.CREDIT));
            else
                criteria.add((Criteria.where("transactType").is(transactType)));
        }
            if(transactorName != null && !transactorName.isEmpty())
            criteria.add(Criteria.where("transactorName").is(transactorName));
        if(transactDesc !=null && !transactDesc.isEmpty())
            criteria.add(Criteria.where("transacDesc").is(transactDesc));
        if(transactRefNo != null)
            criteria.add(Criteria.where("transactRefNo").is(transactRefNo));
        if(transactDetails!=null)
            criteria.add(Criteria.where("transactDetails").is(transactDetails));
        if(transactDate!=null)
            criteria.add(Criteria.where("transactDate").is(transactDate));

//        if(Optional.ofNullable(fromDate).isPresent() && Optional.ofNullable(toDate).isPresent()) {
//            Criteria dateCriteria = new Criteria();
//            dateCriteria.andOperator(
//                    Criteria.where("transactDate").gte(Date.from(fromDate.atStartOfDay().toInstant(ZoneOffset.ofHours(0)))),
//                    Criteria.where("transactDate").lte(Date.from(toDate.atStartOfDay().toInstant(ZoneOffset.ofHours(0)))));
//            query.addCriteria(dateCriteria);
//        }

        Criteria cmncriteria = new Criteria();
        if(!criteria.isEmpty()){
            cmncriteria.orOperator(criteria.toArray(criteria.toArray(new Criteria[criteria.size()])));
        }
//        cmncriteria.andOperator(Criteria.where("activeStatus").is(activeStatus));

        query.addCriteria(cmncriteria);
        query.with(pageable);
        long count = mongoTemplate.count(new Query().addCriteria(cmncriteria), Account.class);
        List<Account> accountResponse = mongoTemplate.find(query,Account.class);
        return new PageImpl<Account>(accountResponse, pageable, count);

//        if(!criteria.isEmpty())
//            query.addCriteria(new Criteria().andOperator(
//                    criteria.toArray(criteria.toArray(new Criteria[criteria.size()]))
//            ));
//
//        List<Account> accounts = mongoTemplate.find(query.with(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"createdDateTime"))),Account.class);
//        return new PageImpl<Account> (accounts, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"createdDate")),accounts.size());
    }

    @Override
    public List<AccountDataSummary> getAggregateResultByAllOperator() {
        Aggregation aggQuery = newAggregation(
                group("createdBy","accountHeadCode","accountHeadName").count().as("receiptCount").sum("amount").as("amountCollected"),
                group("createdBy").push(new BasicDBObject
                        ("accountHeadCode","$_id.accountHeadCode").append
                        ("accountHeadName","$_id.accountHeadName").append
                        ( "receiptCount","$receiptCount").append
                        ("amountCollected","$amountCollected")
                ).as("summary"),
                project("summary").and("$_id").as("operatorCode")

        );
        AggregationResults<AccountDataSummary> accountSummary= mongoTemplate.aggregate(aggQuery, Account.class,AccountDataSummary.class);
        List<AccountDataSummary> accountDataSummary = accountSummary.getMappedResults();
        return accountDataSummary;
    }
}
