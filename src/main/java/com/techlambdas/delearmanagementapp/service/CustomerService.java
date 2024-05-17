package com.techlambdas.delearmanagementapp.service;


import com.techlambdas.delearmanagementapp.model.Customer;
import com.techlambdas.delearmanagementapp.request.CustomerRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {

    Customer createCustomer(CustomerRequest customerRequest);

    List<Customer> getAllCustomers(String customerId, String customerName, String mobileNo, String city);

    Customer getCustomerByCustomerId(String customerId);

    Page<Customer> getAllCustomersWithPage(String customerId, String customerName, String mobileNo, String city, Pageable pageable);


    Customer updateCustomer(String customerId,CustomerRequest customerRequest);

    String deleteByCustomerId(String customerId);


}
