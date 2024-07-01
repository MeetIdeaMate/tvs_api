package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.*;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
public class CustomSalesRepositoryImpl implements  CustomSalesRepository{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Sales> getAllSales(String invoiceNo,String customerName,String mobileNo,String partNo,String paymentType,Boolean isCancelled,PaymentStatus paymentStatus,String billType) {
        Query query=new Query();
    if (Optional.ofNullable(invoiceNo).isPresent()) {
        query.addCriteria(Criteria.where("invoiceNo").is(invoiceNo));
    }
    if (Optional.ofNullable(customerName).isPresent()) {
        List<String> customerNameFromCustomer=getCustomerNameFromCustomer(customerName);
        query.addCriteria(Criteria.where("customerId").in(customerNameFromCustomer));
    }
    if (Optional.ofNullable(mobileNo).isPresent()) {
        List<String> mobileNoFromCustomer = getCustomerMobileFromCustomer(mobileNo);
        query.addCriteria(Criteria.where("customerId").in(mobileNoFromCustomer));
    }
    if (Optional.ofNullable(partNo).isPresent()) {
        query.addCriteria(Criteria.where("itemDetails.partNo").is(partNo));
    }
    if (Optional.ofNullable(paymentType).isPresent()) {
        query.addCriteria(Criteria.where("paidDetails.paymentType").is(paymentType));
    }
    if (Optional.ofNullable(isCancelled).isPresent()){
        query.addCriteria(Criteria.where("isCancelled").is(isCancelled));
    }
    if (Optional.ofNullable(paymentStatus).isPresent()){
        query.addCriteria(Criteria.where("paymentStatus").in(paymentStatus));
    }
    if (Optional.ofNullable(billType).isPresent()){
        query.addCriteria(Criteria.where("billType").regex(Pattern.compile(billType,Pattern.CASE_INSENSITIVE)));
    }
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));

        return mongoTemplate.find(query, Sales.class);
    }

    @Override
    public Page<Sales> getAllSalesWithPage(String invoiceNo, String categoryName,String customerName,String mobileNo,String partNo,String paymentType,Boolean isCancelled,String branchName,String billType,PaymentStatus paymentStatus,LocalDate fromDate , LocalDate toDate ,Pageable pageable ) {
        Query query=new Query();
        if (Optional.ofNullable(invoiceNo).isPresent()) {
            query.addCriteria(Criteria.where("invoiceNo").is(invoiceNo));
        }
        if (Optional.ofNullable(categoryName).isPresent()) {
            List<String> categoryFromCatName = getCategoryNameFromCategory(categoryName);
            query.addCriteria(Criteria.where("itemDetails.categoryId").in(categoryFromCatName));
        }
        if (Optional.ofNullable(customerName).isPresent()) {
            List<String> customerNameFromCustomer=getCustomerNameFromCustomer(customerName);
            query.addCriteria(Criteria.where("customerId").in(customerNameFromCustomer));
        }
        if (Optional.ofNullable(mobileNo).isPresent()) {
            List<String> mobileNoFromCustomer = getCustomerMobileFromCustomer(mobileNo);
            query.addCriteria(Criteria.where("customerId").in(mobileNoFromCustomer));
        }
        if (Optional.ofNullable(partNo).isPresent()) {
            query.addCriteria(Criteria.where("itemDetails.partNo").is(partNo));
        }
        if (Optional.ofNullable(paymentType).isPresent()) {
            query.addCriteria(Criteria.where("paidDetails.paymentType").is(paymentType));
        }
        if (Optional.ofNullable(isCancelled).isPresent()){
            query.addCriteria(Criteria.where("isCancelled").is(isCancelled));
        }
        if (Optional.ofNullable(branchName).isPresent()) {
            List<String> branchNameFromBranch=getBranchNameFromBranch(branchName);
            query.addCriteria(Criteria.where("branchId").in(branchNameFromBranch));
        }
        if (Optional.ofNullable(paymentStatus).isPresent()){
            query.addCriteria(Criteria.where("paymentStatus").in(paymentStatus));
        }
        if (Optional.ofNullable(billType).isPresent()){
            query.addCriteria(Criteria.where("billType").regex(Pattern.compile(billType,Pattern.CASE_INSENSITIVE)));
        }
        if (fromDate != null && toDate != null) {
            query.addCriteria(Criteria.where("invoiceDate").gte(fromDate).lte(toDate));
        }
        else if (fromDate != null) {
            query.addCriteria(Criteria.where("invoiceDate").gte(fromDate));
        } else if (toDate != null) {
            query.addCriteria(Criteria.where("invoiceDate").lte(toDate));
        }
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));

        long count = mongoTemplate.count(query, Sales.class);
        query.with(pageable);
        List<Sales> sales = mongoTemplate.find(query, Sales.class);

        return new PageImpl<>(sales, pageable, count);
    }

    public List<String>getCategoryNameFromCategory(String categoryName)
    {
        Query query=new Query();
        if (categoryName!=null){
            query.addCriteria(Criteria.where("categoryName").regex("^.*"+categoryName+".*","i"));
        }
        List<Category> categoryList=mongoTemplate.find(query, Category.class);
        return categoryList.stream().map(Category::getCategoryId).collect(Collectors.toList());
    }
    public List<String> getCustomerNameFromCustomer(String customerName)
    {
        Query query = new Query();
        if (customerName != null) {
            query.addCriteria(Criteria.where("customerName").regex("^.*" + customerName + ".*", "i"));
        }
        List<Customer> customerList = mongoTemplate.find(query, Customer.class);
        return customerList.stream().map(Customer::getCustomerId).collect(Collectors.toList());
    }
    public List<String> getCustomerMobileFromCustomer(String mobileNo)
    {
        Query query = new Query();
        if (mobileNo != null) {
            query.addCriteria(Criteria.where("mobileNo").regex("^.*" + mobileNo + ".*", "i"));
        }
        List<Customer> customerList = mongoTemplate.find(query, Customer.class);
        return customerList.stream().map(Customer::getCustomerId).collect(Collectors.toList());
    }
    public List<String> getBranchNameFromBranch(String branchName)
    {
        Query query=new Query();
        if (branchName!=null)
        {
            query.addCriteria(Criteria.where("branchName").regex("^.*"+branchName+".*","i"));
        }
        List<Branch> branches=mongoTemplate.find(query,Branch.class);
        return branches.stream().map(Branch::getBranchId).collect(Collectors.toList());
    }
}
