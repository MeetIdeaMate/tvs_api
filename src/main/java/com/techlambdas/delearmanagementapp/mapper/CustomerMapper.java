package com.techlambdas.delearmanagementapp.mapper;

import com.techlambdas.delearmanagementapp.model.Customer;
import com.techlambdas.delearmanagementapp.request.CustomerRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer customerRequestToCustomer(CustomerRequest request);
    void updateCustomerFromRequest(CustomerRequest request, @MappingTarget Customer customer);
}