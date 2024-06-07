package com.techlambdas.delearmanagementapp.controller;

import com.techlambdas.delearmanagementapp.model.Stock;
import com.techlambdas.delearmanagementapp.request.StockRequest;
import com.techlambdas.delearmanagementapp.response.StockResponse;
import com.techlambdas.delearmanagementapp.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.techlambdas.delearmanagementapp.response.AppResponse.successResponse;

@RestController
@RequestMapping("/stock")
public class StockController {
    @Autowired
    private StockService stockService;
    @PostMapping
    public ResponseEntity<Stock> createStock(@RequestBody StockRequest stockRequest) {
        Stock stock = stockService.createStock(stockRequest);
        return successResponse(HttpStatus.CREATED,"stock",stock);
    }
    @GetMapping
    public ResponseEntity<List<Stock>> getAllStockS(@RequestParam(required = false) String partNo,
                                                    @RequestParam(required = false) String itemName,
                                                    @RequestParam(required = false) String engineNo,
                                                    @RequestParam(required = false) String frameNo)
    {
        List<StockResponse> stocks=stockService.getAllStocks(partNo,itemName,engineNo,frameNo);
        return successResponse(HttpStatus.CREATED,"Stocks",stocks);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStockDetails(@PathVariable String id,@RequestBody StockRequest stockRequest)
    {
        Stock stock=stockService.updateStockDetails(id,stockRequest);
        return successResponse(HttpStatus.CREATED,"UpdatedStock",stock);
    }
    @GetMapping("/page")
    public ResponseEntity<Page<Stock>> getAllStockWithPage(@RequestParam(required = false) String partNo,
                                                           @RequestParam(required = false) String itemName,
                                                           @RequestParam(required = false) String engineNo,
                                                           @RequestParam(required = false) String frameNo,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable = PageRequest.of(page,size);
        Page<Stock> stockPage = stockService.getAllStocksWithPage(partNo,itemName,engineNo,frameNo, pageable);
        return successResponse(HttpStatus.CREATED,"stockWithPage",stockPage);
    }
}
