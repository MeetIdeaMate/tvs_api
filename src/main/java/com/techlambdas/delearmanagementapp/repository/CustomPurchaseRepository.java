package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Purchase;
import com.techlambdas.delearmanagementapp.response.PurchaseReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CustomPurchaseRepository {

    List<Purchase> getAllPurchases(String purchaseNo, String pInvoiceNo, String pOrderRefNo,LocalDate fromDate,LocalDate toDate,String categoryName,String branchId,Boolean isStockUpdated);

    Page<Purchase> getAllPurchasesWithPage(String purchaseNo, String pInvoiceNo, String pOrderRefNo, Pageable pageable, LocalDate fromDate,LocalDate toDate,String categoryName,String branchId,Boolean isStockUpdated);

    Purchase findLastPurchaseItemDetailsByPartNo(String partNo);

    List<PurchaseReport> getPurchaseReport(Date fromDate, Date toDate);

    Purchase findPuechaseByPartNoAndMainspecValue(String partNo, Map<String, String> mainSpecValue);
}
