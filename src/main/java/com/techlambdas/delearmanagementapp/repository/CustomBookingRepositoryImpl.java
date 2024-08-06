package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.constant.BookingStatus;
import com.techlambdas.delearmanagementapp.constant.PaymentType;
import com.techlambdas.delearmanagementapp.model.Booking;
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
    public List<Booking> getAllBookings(String bookingNo, String customerName, PaymentType paymentType, String branchId, String branchName, LocalDate fromDate, LocalDate toDate,BookingStatus bookingStatus) {
        Query query=new Query();
        if (Optional.ofNullable(bookingNo).isPresent())
            query.addCriteria(Criteria.where("bookingNo").is(bookingNo));
        if (Optional.ofNullable(customerName).isPresent()) {
            List<String> customerIds = getCustomerIdFromCustomers(customerName);
            query.addCriteria(Criteria.where("customerId").in(customerIds));
        }
        if (Optional.ofNullable(paymentType).isPresent())
            query.addCriteria(Criteria.where("paidDetail.paymentType").is(paymentType));
        if (Optional.ofNullable(fromDate).isPresent() && Optional.ofNullable(toDate).isPresent()){
            query.addCriteria(Criteria.where("bookingDate").gte(fromDate).lte(toDate));
        }
        if (Optional.ofNullable(branchName).isPresent()){
            List<String> branchIds = getBranchNameFromBranch(branchName);
            query.addCriteria(Criteria.where("branchId").in(branchIds));
        }
        if (Optional.ofNullable(branchId).isPresent())
        {
            query.addCriteria(Criteria.where("branchId").is(branchId));
        }if (Optional.ofNullable(bookingStatus).isPresent()){
            query.addCriteria(Criteria.where("bookingStatus").is(bookingStatus));
        }else {
            query.addCriteria(Criteria.where("bookingStatus").ne(BookingStatus.CANCELLED));

        }
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));

        return mongoTemplate.find(query,Booking.class);
    }

    @Override
    public Page<Booking> getAllBookingsWithPage(String bookingNo, String customerName, PaymentType paymentType,String branchId,String branchName,LocalDate fromDate, LocalDate toDate, Pageable pageable,BookingStatus bookingStatus) {
        Query query=new Query();
        if (Optional.ofNullable(bookingNo).isPresent())
            query.addCriteria(Criteria.where("bookingNo").is(bookingNo));
        if (Optional.ofNullable(customerName).isPresent()) {
            List<String> customerIds = getCustomerIdFromCustomers(customerName);
            query.addCriteria(Criteria.where("customerId").in(customerIds));
        }
        if (Optional.ofNullable(paymentType).isPresent())
            query.addCriteria(Criteria.where("paidDetail.paymentType").is(paymentType));
        if (Optional.ofNullable(fromDate).isPresent() && Optional.ofNullable(toDate).isPresent()){
            query.addCriteria(Criteria.where("bookingDate").gte(fromDate).lte(toDate));
        }
        if (Optional.ofNullable(branchName).isPresent()){
            List<String> branchIds = getBranchNameFromBranch(branchName);
            query.addCriteria(Criteria.where("branchId").in(branchIds));
        }
        if (Optional.ofNullable(branchId).isPresent())
        {
            query.addCriteria(Criteria.where("branchId").is(branchId));
        }
        if (Optional.ofNullable(bookingStatus).isPresent()){
            query.addCriteria(Criteria.where("bookingStatus").is(bookingStatus));
        }else {
            query.addCriteria(Criteria.where("bookingStatus").ne(BookingStatus.CANCELLED));

        }
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));
        long count=mongoTemplate.count(query,Booking.class);
        query.with(pageable);
        List<Booking> bookings=mongoTemplate.find(query,Booking.class);
        return new PageImpl<>(bookings,pageable, count);
    }

    @Override
    public List<Booking> findBookingByCustomerId(String customerId) {
        Query query=new Query();
        if (Optional.ofNullable(customerId).isPresent())
            query.addCriteria(Criteria.where("customerId").is(customerId));
        query.addCriteria(Criteria.where("bookingStatus").is(BookingStatus.INPROGRESS));
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));
        return mongoTemplate.find(query,Booking.class);
    }

    public List<String>getBranchNameFromBranch(String branchName)
    {
        Query query=new Query();
        if (Optional.ofNullable(branchName).isPresent()){
            query.addCriteria(Criteria.where("branchName").regex("^.*"+branchName+".*","i"));
        }
        List<Branch> brancheList=mongoTemplate.find(query, Branch.class);
        return brancheList.stream().map(Branch::getBranchId).collect(Collectors.toList());
    }
    public List<String>getCustomerIdFromCustomers(String customerName)
    {
        Query query=new Query();
        if (Optional.ofNullable(customerName).isPresent()){
            query.addCriteria(Criteria.where("customerName").regex("^.*"+customerName+".*","i"));
        }
        List<Customer> customers=mongoTemplate.find(query, Customer.class);
        return customers.stream().map(Customer::getCustomerId).collect(Collectors.toList());
    }
}
