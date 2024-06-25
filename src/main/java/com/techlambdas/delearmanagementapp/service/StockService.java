package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.constant.TransferStatus;
import com.techlambdas.delearmanagementapp.constant.TransferType;
import com.techlambdas.delearmanagementapp.model.Sales;
import com.techlambdas.delearmanagementapp.model.Stock;
import com.techlambdas.delearmanagementapp.request.SalesRequest;
import com.techlambdas.delearmanagementapp.request.StockAddReq;
import com.techlambdas.delearmanagementapp.request.StockRequest;
import com.techlambdas.delearmanagementapp.request.TransferRequest;
import com.techlambdas.delearmanagementapp.response.PurchaseResponse;
import com.techlambdas.delearmanagementapp.response.StockDTO;
import com.techlambdas.delearmanagementapp.response.StockResponse;
import com.techlambdas.delearmanagementapp.response.TransferResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StockService {
    Stock createStock(StockRequest stockRequest);

    List<StockResponse> getAllStocks(String partNo, String itemName,String keyValue,String categoryName,String branchId);

//    Stock updateStockDetails(String id, StockRequest stockRequest);

    Page<StockResponse> getAllStocksWithPage(String partNo,String itemName,String keyValue, Pageable pageable,String categoryName,String branchId);

    List<StockResponse> createStockFromPurchase(String purchaseId, StockAddReq stockAddReq);

  void updateSalesInfoToStock(Sales sales);

    String createTransfer(TransferRequest transferRequest);

    List<TransferResponse> getTransferDetails(String fromBranchId,String toBranchId, TransferStatus transferStatus, TransferType transferType);


    String approveTransfer(String branchId, String transferId);

    Page<StockDTO> getCumulativeStockWithPage(String partNo, String itemName, String keyValue, Pageable pageable, String categoryName, String branchId);
}
