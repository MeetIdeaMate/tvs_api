package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.constant.TransferStatus;
import com.techlambdas.delearmanagementapp.model.Sales;
import com.techlambdas.delearmanagementapp.model.Stock;
import com.techlambdas.delearmanagementapp.request.SalesRequest;
import com.techlambdas.delearmanagementapp.request.StockAddReq;
import com.techlambdas.delearmanagementapp.request.StockRequest;
import com.techlambdas.delearmanagementapp.request.TransferRequest;
import com.techlambdas.delearmanagementapp.response.PurchaseResponse;
import com.techlambdas.delearmanagementapp.response.StockResponse;
import com.techlambdas.delearmanagementapp.response.TransferResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StockService {
    Stock createStock(StockRequest stockRequest);

    List<StockResponse> getAllStocks(String partNo, String itemName,String engineNo,String frameNo,String categoryName);

    Stock updateStockDetails(String id, StockRequest stockRequest);

    Page<StockResponse> getAllStocksWithPage(String partNo,String itemName,String engineNo,String frameNo, Pageable pageable,String categoryName);

    List<StockResponse> createStockFromPurchase(String purchaseId, StockAddReq stockAddReq);

    void mapSalesRequestToStock(SalesRequest salesRequest);

    String createTransfer(TransferRequest transferRequest);

    List<TransferResponse> getTransferDetails(String branchId, TransferStatus transferStatus);

    List<TransferResponse> getTransferReceivedDetails(String branchId, TransferStatus transferStatus);

    String approveTransfer(String branchId, String transferId);
}
