package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Stock;
import com.techlambdas.delearmanagementapp.response.StockDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends MongoRepository<Stock,String> {
    Stock findStockByStockId(String stockId);

    List<Stock> findPartNoByBranchId(String branchId);

}
