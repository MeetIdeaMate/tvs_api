package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends MongoRepository<Stock,String> {
    Stock findStockById(String id);
}
