package com.techlambdas.delearmanagementapp.repository;
import com.techlambdas.delearmanagementapp.constant.TransferType;
import com.techlambdas.delearmanagementapp.model.Stock;
import com.techlambdas.delearmanagementapp.response.StockDTO;
import com.techlambdas.delearmanagementapp.response.TransferResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomStockRepository {
    List<Stock> getAllStocks(String partNo,String itemName,String keyValue,String categoryName,String branchId);

    Page<Stock> getAllStocksWithPage(String partNo,String itemName,String keyValue,Pageable pageable,String categoryName,String branchId);

    List<Stock> findByPartNoAndBranchId(String partNo, String transferFromBranch);

    List<TransferResponse> findTransferDetails(String fromBranchId,String toBranchId, TransferType transferType);

    List<Stock> findStocksByTransferId(String transferId);

    Page<StockDTO> getCumulativeStockWithPage(String partNo, String itemName, String keyValue, Pageable pageable, String categoryName, String branchId);
}
