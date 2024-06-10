package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.CommonMapper;
import com.techlambdas.delearmanagementapp.mapper.SalesMapper;
import com.techlambdas.delearmanagementapp.mapper.StockMapper;
import com.techlambdas.delearmanagementapp.model.*;
import com.techlambdas.delearmanagementapp.repository.CustomStockRepository;
import com.techlambdas.delearmanagementapp.repository.PurchaseRepository;
import com.techlambdas.delearmanagementapp.repository.SalesRepository;
import com.techlambdas.delearmanagementapp.repository.StockRepository;
import com.techlambdas.delearmanagementapp.request.SalesRequest;
import com.techlambdas.delearmanagementapp.request.StockRequest;
import com.techlambdas.delearmanagementapp.response.StockResponse;
import com.techlambdas.delearmanagementapp.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl implements StockService{
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private CustomStockRepository customStockRepository;
    @Autowired
    private CommonMapper commonMapper;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private SalesRepository salesRepository;
    @Autowired
    private SalesMapper salesMapper;
    @Override
    public Stock createStock(StockRequest stockRequest) {
        try {
            Stock stock = stockMapper.mapStockRequestToStock(stockRequest);
            stock.setStockId(RandomIdGenerator.getRandomId());
            return stockRepository.save(stock);
        }catch (Exception ex) {
            throw new RuntimeException("Internel Server Error -- " + ex.getMessage(),ex.getCause());
        }
    }

    @Override
    public List<StockResponse> getAllStocks(String partNo, String itemName, String engineNo, String frameNo) {
        List<Stock> stocks=customStockRepository.getAllStocks(partNo,itemName,engineNo,frameNo);
        return stocks.stream()
                .map(commonMapper:: toStockResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Stock updateStockDetails(String id, StockRequest stockRequest) {
        try {
            Stock existingStock=stockRepository.findStockById(id);
            if (existingStock==null)
                throw new DataNotFoundException("stocks not found with Id"+id);
            stockMapper.updateStockDetailFromRequest(stockRequest,existingStock);
            return stockRepository.save(existingStock);
        }catch (DataNotFoundException ex){
            throw new DataNotFoundException("Data not found -- "+ex.getMessage());
        }catch (Exception ex){
            throw new RuntimeException("Internal Server Error --"+ex.getMessage(),ex.getCause());
        }
    }

    @Override
    public Page<Stock> getAllStocksWithPage(String partNo,String itemName,String engineNo,String frameNo, Pageable pageable) {
        return customStockRepository.getAllStocksWithPage(partNo,itemName,engineNo,frameNo,pageable);
    }

    @Override
    public List<StockResponse> createStockFromPurchase(String purchaseId, List<String> partNo) {
        Optional<Purchase> purchaseOptional = purchaseRepository.findByPurchaseId(purchaseId);
        if (!purchaseOptional.isPresent()) {
            throw new DataNotFoundException("Purchase not found with id: " + purchaseId);
        }

        Purchase purchase = purchaseOptional.get();
        List<Stock> stocks = new ArrayList<>();

        for (ItemDetail itemDetail : purchase.getItemDetails()) {
            if (partNo.contains(itemDetail.getPartNo())) {
                Stock stock = stockMapper.itemDetailToStock(itemDetail, purchase.getBranchId());
                PurchaseItem purchaseItem = stockMapper.itemDetailToPurchaseItem(itemDetail);
                SalesItem salesItem = stockMapper.itemDetailToSalesItem(itemDetail);
                stock.setPurchaseItem(purchaseItem);
                stock.setSalesItem(salesItem);
                stockRepository.save(stock);
                stocks.add(stock);
            }
        }

        return stocks.stream()
                .map(commonMapper::toStockResponse)
                .collect(Collectors.toList());
    }
    public void mapSalesRequestToStock(SalesRequest salesRequest)
    {
        for (ItemDetail itemDetail:salesRequest.getItemDetails()) {
            Stock stock = stockMapper.itemDetailToStock(itemDetail, salesRequest.getBranchId());
            PurchaseItem purchaseItem = stockMapper.itemDetailToPurchaseItem(itemDetail);
            SalesItem salesItem = stockMapper.itemDetailToSalesItem(itemDetail);

            stock.setPurchaseItem(purchaseItem);
            stock.setSalesItem(salesItem);
            stockRepository.save(stock);
        }
    }
}
