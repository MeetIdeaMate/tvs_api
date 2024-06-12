package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Stock;
import com.techlambdas.delearmanagementapp.model.TransferDetail;
import com.techlambdas.delearmanagementapp.response.TransferResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomStockRepository {
    List<Stock> getAllStocks(String partNo,String itemName,String engineNo,String frameNo,String categoryName);

    Page<Stock> getAllStocksWithPage(String partNo,String itemName, String engineNo,String frameNo,Pageable pageable,String categoryName);

    List<Stock> findByPartNoAndBranchId(String partNo, String transferFromBranch);

    List<TransferResponse> findTransferDetails(String branchId);
}
