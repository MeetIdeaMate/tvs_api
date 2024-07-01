package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomCustomerRepository {
    List<Customer> getAllCustomers(String customerId, String customerName, String mobileNo, String city,String branchId,String branchName);

    Page<Customer> getAllCustomersWithPage(String customerId, String customerName, String mobileNo, String city,String branchId,String branchName, Pageable pageable);
}
