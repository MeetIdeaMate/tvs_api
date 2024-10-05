package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Customer;
import com.techlambdas.delearmanagementapp.model.Insurance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InsuranceCustomRepoImpl implements InsuranceCustomRepository{

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Page<Insurance> getAllInsuranceWithPagination(String customerName, String mobileNo, String invoiceNo, String insuranceCompanyName, Pageable pageable) {
        Query query = new Query();
        if(invoiceNo!=null)
            query.addCriteria(Criteria.where("invoiceNo").is(invoiceNo));
        if(insuranceCompanyName!=null)
            query.addCriteria(Criteria.where("insuranceCompanyName").is(insuranceCompanyName));
        if(customerName!= null)
        {
            List<String> customerId = findCustomer(customerName, mobileNo);
            query.addCriteria(Criteria.where("customerId").is(customerId));
        }
        if(mobileNo!=null){
            List<String> customerId = findCustomer(customerName, mobileNo);
            query.addCriteria(Criteria.where("customerName").is(customerName));
        }
     long totalCount = mongoTemplate.count(query,Insurance.class);
        query.with(pageable);
        List<Insurance> insuranceList = mongoTemplate.find(query,Insurance.class);
        return new PageImpl<>(insuranceList,pageable,totalCount);
    }

    private List<String> findCustomer(String customerName, String mobileNo) {
        Query query = new Query();
        if(customerName!=null)
            query.addCriteria(Criteria.where("customerName").is(customerName));
        if(mobileNo!=null)
            query.addCriteria(Criteria.where("mobileNo").is(mobileNo));
       List<Customer> customer = mongoTemplate.find(query,Customer.class);
      return customer.stream().map(Customer::getCustomerId).collect(Collectors.toList());
    }
}

