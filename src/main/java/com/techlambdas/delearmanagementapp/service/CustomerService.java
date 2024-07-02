package com.techlambdas.delearmanagementapp.service;


import com.techlambdas.delearmanagementapp.model.Customer;
import com.techlambdas.delearmanagementapp.request.CustomerRequest;
import com.techlambdas.delearmanagementapp.response.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerRequest customerRequest);

    List<CustomerResponse> getAllCustomers(String customerId, String customerName, String mobileNo, String city,String branchId,String branchName);

    Customer getCustomerByCustomerId(String customerId);

    Page<CustomerResponse> getAllCustomersWithPage(String customerId, String customerName, String mobileNo, String city,String branchId,String branchName, Pageable pageable);


    Customer updateCustomer(String customerId,CustomerRequest customerRequest);

    String deleteByCustomerId(String customerId);


}
