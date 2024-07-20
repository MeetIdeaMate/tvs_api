package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.exception.AlreadyExistException;
import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.CommonMapper;
import com.techlambdas.delearmanagementapp.mapper.CustomerMapper;
import com.techlambdas.delearmanagementapp.model.Customer;
import com.techlambdas.delearmanagementapp.repository.CustomCustomerRepository;
import com.techlambdas.delearmanagementapp.repository.CustomerRepository;
import com.techlambdas.delearmanagementapp.request.CustomerRequest;
import com.techlambdas.delearmanagementapp.response.CustomerResponse;
import com.techlambdas.delearmanagementapp.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CommonMapper commonMapper;
    @Autowired
    private CustomCustomerRepository customCustomerRepository;

    @Override
    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        try {
            Customer existingCustomer=customerRepository.findCustomerByMobileNo(customerRequest.getMobileNo());
            if (existingCustomer!=null)
                throw new AlreadyExistException("MobileNumber Already Exist:"+existingCustomer.getMobileNo());
            Customer customer =  customerMapper.customerRequestToCustomer(customerRequest);
            String generatedId= RandomIdGenerator.getRandomId();
            customer.setCustomerId(generatedId);
           Customer createdCustomer= customerRepository.save(customer);
           return commonMapper.mapToCustomerResponse(createdCustomer);
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public List<CustomerResponse> getAllCustomers(String customerId, String customerName, String mobileNo, String city) {
        List<Customer>customers=customCustomerRepository.getAllCustomers(customerId,customerName,mobileNo,city);
        return commonMapper.mapToCustomerResponses(customers);
    }

    @Override
    public Customer getCustomerByCustomerId(String customerId) {
        Customer customer=customerRepository.findByCustomerId(customerId);
        if (customer==null)
            throw new DataNotFoundException("Customer not found with ID: " + customerId);
        return customer;
    }

    @Override
    public Page<CustomerResponse> getAllCustomersWithPage(String customerId, String customerName, String mobileNo, String city, Pageable pageable) {

       Page<Customer>customerPage= customCustomerRepository.getAllCustomersWithPage(customerId,customerName,mobileNo,city,pageable);
       List<CustomerResponse>customerResponses=commonMapper.mapToCustomerResponses(customerPage.getContent());
       return new PageImpl<>(customerResponses,pageable,customerPage.getTotalElements());
    }

    @Override
    public Customer updateCustomer(String customerId, CustomerRequest customerRequest) {
        try {
            Customer existingCustomer = customerRepository.findByCustomerId(customerId);
            if (existingCustomer == null)
                throw new DataNotFoundException("Customer not found with ID: " + customerId);
            customerMapper.updateCustomerFromRequest(customerRequest, existingCustomer);
            return customerRepository.save(existingCustomer);
        }catch (DataNotFoundException ex) {
            throw new DataNotFoundException("Data not found --" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }
    @Override
    public String deleteByCustomerId(String customerId) {
        try {
            Customer existingCustomer = customerRepository.findByCustomerId(customerId);
            if (existingCustomer == null)
                throw new DataNotFoundException("Customer not found with ID: " + customerId);
            customerRepository.delete(existingCustomer);
            return "deleted";
        } catch (DataNotFoundException ex) {
            throw new DataNotFoundException("Data not found --" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }


}
