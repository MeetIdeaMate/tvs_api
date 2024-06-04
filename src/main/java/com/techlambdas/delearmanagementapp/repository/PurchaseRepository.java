package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends MongoRepository<Purchase,String>  {
    Purchase findByPurchaseNo(String purchaseNo);

    @Query(value = "{ 'itemDetails.partNo': ?0 }", sort = "{ 'p_invoiceDate': 1 }")
    Optional<Purchase> findTopByPartNoOrderByInvoiceDateDesc(String partNo);
}
