package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends MongoRepository<Stock,String> {
    Stock findStockById(String id);

    List<Stock> findPartNoByBranchId(String branchId);
}
