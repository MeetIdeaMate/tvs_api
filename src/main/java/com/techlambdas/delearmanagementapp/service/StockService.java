package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.model.Stock;
import com.techlambdas.delearmanagementapp.request.StockRequest;
import com.techlambdas.delearmanagementapp.response.StockResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StockService {
    Stock createStock(StockRequest stockRequest);

    List<StockResponse> getAllStocks(String partNo, String itemName,String engineNo,String frameNo);

    Stock updateStockDetails(String id, StockRequest stockRequest);

    Page<Stock> getAllStocksWithPage(String partNo,String itemName,String engineNo,String frameNo, Pageable pageable);
}
