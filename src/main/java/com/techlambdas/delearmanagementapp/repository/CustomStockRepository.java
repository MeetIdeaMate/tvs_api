package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomStockRepository {
    List<Stock> getAllStocks(String partNo,String itemName,String engineNo,String frameNo);

    Page<Stock> getAllStocksWithPage(String partNo,String itemName, String engineNo,String frameNo,Pageable pageable);
}
