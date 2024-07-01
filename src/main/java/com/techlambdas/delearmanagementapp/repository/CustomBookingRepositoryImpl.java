package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Booking;
import com.techlambdas.delearmanagementapp.model.Branch;
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
public class CustomBookingRepositoryImpl implements CustomBookingRepository{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Booking> getAllBookings(String bookingNo, String customerName, String paymentType,String branchId,String branchName, LocalDate fromDate, LocalDate toDate) {
        Query query=new Query();
        if (Optional.ofNullable(bookingNo).isPresent())
            query.addCriteria(Criteria.where("bookingNo").is(bookingNo));
        if (Optional.ofNullable(customerName).isPresent())
            query.addCriteria(Criteria.where("customerName").regex(Pattern.compile(customerName,Pattern.CASE_INSENSITIVE)));
        if (Optional.ofNullable(paymentType).isPresent())
            query.addCriteria(Criteria.where("paymentType").regex(Pattern.compile(paymentType,Pattern.CASE_INSENSITIVE)));
        if (Optional.ofNullable(fromDate).isPresent() && Optional.ofNullable(toDate).isPresent()){
            query.addCriteria(Criteria.where("bookingDate").gte(fromDate).lte(toDate));
        }
        if (branchName != null) {
            List<String> branchIds = getBranchNameFromBranch(branchName);
            query.addCriteria(Criteria.where("branchId").in(branchIds));
        }
        if (branchId !=null)
        {
            query.addCriteria(Criteria.where("branchId").is(branchId));
        }
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));

        return mongoTemplate.find(query,Booking.class);
    }

    @Override
    public Page<Booking> getAllBookingsWithPage(String bookingNo, String customerName, String paymentType,String branchId,String branchName,LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        Query query=new Query();
        if (Optional.ofNullable(bookingNo).isPresent())
            query.addCriteria(Criteria.where("bookingNo").is(bookingNo));
        if (Optional.ofNullable(customerName).isPresent())
            query.addCriteria(Criteria.where("customerName").regex(Pattern.compile(customerName,Pattern.CASE_INSENSITIVE)));
        if (Optional.ofNullable(paymentType).isPresent())
            query.addCriteria(Criteria.where("paymentType").regex(Pattern.compile(paymentType,Pattern.CASE_INSENSITIVE)));
        if (Optional.ofNullable(fromDate).isPresent() && Optional.ofNullable(toDate).isPresent()){
            query.addCriteria(Criteria.where("bookingDate").gte(fromDate).lte(toDate));
        }
        if (branchName != null) {
            List<String> branchIds = getBranchNameFromBranch(branchName);
            query.addCriteria(Criteria.where("branchId").in(branchIds));
        }
        if (branchId !=null)
        {
            query.addCriteria(Criteria.where("branchId").is(branchId));
        }
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));
        long count=mongoTemplate.count(query,Booking.class);
        query.with(pageable);
        List<Booking> bookings=mongoTemplate.find(query,Booking.class);
        return new PageImpl<>(bookings,pageable, count);
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
