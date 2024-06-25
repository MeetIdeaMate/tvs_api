package com.techlambdas.delearmanagementapp.repository;


import com.techlambdas.delearmanagementapp.model.Branch;
import com.techlambdas.delearmanagementapp.model.Customer;
import com.techlambdas.delearmanagementapp.model.Sales;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<Customer,String> {
    Customer findByCustomerId(String customerId);


    default String getCustomerName(String customerId)
    {
         Customer customer = findByCustomerId(customerId);
        return (customer !=null)?customer.getCustomerName():"unknown customer";
    }

    default String getMobileNo(String customerId)
    {
        Customer customer = findByCustomerId(customerId);
        return (customer !=null)?customer.getMobileNo():"unknown customer";
    }

    default String getAddress(String customerId)
    {
        Customer customer=findByCustomerId(customerId);
        return (customer!=null)?customer.getAddress():"unknown customer";
    }
}
