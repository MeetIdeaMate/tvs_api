package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Branch;
import com.techlambdas.delearmanagementapp.model.Customer;
import com.techlambdas.delearmanagementapp.response.BranchResponse;
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

@Repository
public class CustomBranchRepositoryImpl implements CustomBranchRepository {


    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public List<Branch> getAllBranches(String branchId, String branchName, String mobileNo, String city) {
        Query query=new Query();
        if (branchId!=null)
            query.addCriteria(Criteria.where("branchId").is(branchId));
        if (branchName != null)
            query.addCriteria(Criteria.where("branchName").regex(Pattern.compile(branchName, Pattern.CASE_INSENSITIVE)));
        if (mobileNo!=null)
            query.addCriteria(Criteria.where("mobileNo").regex(mobileNo));
        if (city!=null)
            query.addCriteria(Criteria.where("city").regex(Pattern.compile(city,Pattern.CASE_INSENSITIVE)));
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));

        return mongoTemplate.find(query, Branch.class);

    }

    @Override
    public Page<Branch> getAllBranchesWithPage(String branchId, String branchName, String mobileNo, String city, Pageable pageable) {
        Query query=new Query();
        if (branchId!=null)
            query.addCriteria(Criteria.where("branchId").is(branchId));
        if (branchName != null)
            query.addCriteria(Criteria.where("branchName").regex(Pattern.compile(branchName, Pattern.CASE_INSENSITIVE)));
        if (mobileNo!=null)
            query.addCriteria(Criteria.where("mobileNo").regex(mobileNo));
        if (city!=null)
            query.addCriteria(Criteria.where("city").regex(Pattern.compile(city,Pattern.CASE_INSENSITIVE)));
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));
        long count = mongoTemplate.count(query, Customer.class);
        query.with(pageable);
        List<Branch> branches = mongoTemplate.find(query, Branch.class);

        return new PageImpl<>(branches, pageable, count);
    }

}
