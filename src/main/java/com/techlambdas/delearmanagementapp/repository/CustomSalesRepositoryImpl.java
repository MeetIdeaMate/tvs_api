package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Purchase;
import com.techlambdas.delearmanagementapp.model.Sales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

public class CustomSalesRepositoryImpl implements  CustomSalesRepository{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Sales> getAllSales(String invoiceNo) {
        Query query=new Query();
    if (!Optional.ofNullable(invoiceNo).isPresent())
        query.addCriteria(Criteria.where("invoiceNo").is(invoiceNo));
        return mongoTemplate.find(query, Sales.class);
    }

}
