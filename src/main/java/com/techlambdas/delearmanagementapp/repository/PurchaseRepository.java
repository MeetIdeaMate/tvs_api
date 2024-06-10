package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseRepository extends MongoRepository<Purchase,String>  {
    Purchase findByPurchaseNo(String purchaseNo);

    Optional<Purchase> findByPurchaseId(String purchaseId);
}
