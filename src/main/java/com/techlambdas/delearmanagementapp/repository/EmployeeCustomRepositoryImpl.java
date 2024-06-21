package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Branch;
import com.techlambdas.delearmanagementapp.model.Employee;
import com.techlambdas.delearmanagementapp.model.Item;
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
import java.util.stream.Collectors;

@Repository
public class EmployeeCustomRepositoryImpl implements EmployeeCustomRepository{
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public Page<Employee> getAllEmployeesWithPage(String employeeName, String mobileNumber, String designation,String branchId,String branchName, Pageable pageable) {
            Query query = new Query();

            if (employeeName != null) {
                Criteria criteria = Criteria.where("employeeName").regex("^" + employeeName, "i");
                query.addCriteria(criteria);
            }
        if (branchName != null) {
            List<String> branchIds = getBranchNameFromBranch(branchName);
            query.addCriteria(Criteria.where("branchId").in(branchIds));
        }
            if (mobileNumber!=null) {
                query.addCriteria(Criteria.where("mobileNumber").regex("^.*" + mobileNumber + ".*", "i"));
            }
        if (designation!=null) {
            query.addCriteria(Criteria.where("designation").regex("^.*" + designation + ".*", "i"));
        }
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));

        long totalCount = mongoTemplate.count(query, Employee.class);
            query.with(pageable);
            List<Employee> employeeList = mongoTemplate.find(query, Employee.class);

            return new PageImpl<>(employeeList, pageable, totalCount);
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
