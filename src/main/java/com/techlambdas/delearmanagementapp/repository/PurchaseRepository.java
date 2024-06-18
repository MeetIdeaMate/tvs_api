package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseRepository extends MongoRepository<Purchase,String>  {
    Purchase findByPurchaseNo(String purchaseNo);

    @Query(value = "{ 'itemDetails.partNo': ?0 }", sort = "{ 'p_invoiceDate': 1 }")
    Optional<Purchase> findTopByPartNoOrderByInvoiceDateDesc(String partNo);

    Optional<Purchase> findByPurchaseId(String purchaseId);
}
