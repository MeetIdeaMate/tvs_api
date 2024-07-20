package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Branch;
import com.techlambdas.delearmanagementapp.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
public class CustomCustomerRepositoryImpl implements CustomCustomerRepository{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Customer> getAllCustomers(String customerId, String customerName, String mobileNo, String city) {

        Query query=new Query();
        if (customerId!=null)
            query.addCriteria(Criteria.where("customerId").is(customerId));
        if (customerName != null)
            query.addCriteria(Criteria.where("customerName").regex(Pattern.compile(customerName, Pattern.CASE_INSENSITIVE)));
        if (mobileNo!=null)
            query.addCriteria(Criteria.where("mobileNo").regex(mobileNo));
//        if (branchName != null) {
//            List<String> branchIds = getBranchNameFromBranch(branchName);
//            query.addCriteria(Criteria.where("branchId").in(branchIds));
//        }
//        if (branchId !=null)
//        {
//            query.addCriteria(Criteria.where("branchId").is(branchId));
//        }
        if (city!=null)
            query.addCriteria(Criteria.where("city").regex(Pattern.compile(city,Pattern.CASE_INSENSITIVE)));
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));
        return mongoTemplate.find(query, Customer.class);
    }

    @Override
    public Page<Customer> getAllCustomersWithPage(String customerId, String customerName, String mobileNo, String city, Pageable pageable) {
        Query query=new Query();
        if (customerId!=null)
            query.addCriteria(Criteria.where("customerId").is(customerId));
        if (customerName != null)
            query.addCriteria(Criteria.where("customerName").regex(Pattern.compile(customerName, Pattern.CASE_INSENSITIVE)));
//        if (branchName != null) {
//            List<String> branchIds = getBranchNameFromBranch(branchName);
//            query.addCriteria(Criteria.where("branchId").in(branchIds));
//        }
//        if (branchId !=null)
//        {
//            query.addCriteria(Criteria.where("branchId").is(branchId));
//        }
        if (mobileNo!=null)
            query.addCriteria(Criteria.where("mobileNo").regex(mobileNo));
        if (city!=null)
            query.addCriteria(Criteria.where("city").regex(Pattern.compile(city,Pattern.CASE_INSENSITIVE)));
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));
        long count = mongoTemplate.count(query, Customer.class);
        query.with(pageable);
        List<Customer> customers = mongoTemplate.find(query, Customer.class);

        return new PageImpl<>(customers, pageable, count);
    }
    public List<String>getBranchNameFromBranch(String branchName)
    {
        Query query=new Query();
        if (branchName!=null){
            query.addCriteria(Criteria.where("branchName").regex("^.*"+branchName+".*","i"));
        }
        List<Branch> brancheList=mongoTemplate.find(query, Branch.class);
        return brancheList.stream().map(Branch::getBranchId).collect(Collectors.toList());
    }
}

