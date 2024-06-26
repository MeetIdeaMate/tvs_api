package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Sales;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SalesRepository extends MongoRepository<Sales,String> {
    Sales findByCustomerId(String customerId);

    Sales findByInvoiceNo(String invoiceNo);

//  List<Sales> findByCustomerName(String customerName);


}
