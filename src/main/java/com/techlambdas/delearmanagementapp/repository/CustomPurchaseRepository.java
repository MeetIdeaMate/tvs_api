package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Purchase;
import com.techlambdas.delearmanagementapp.response.PurchaseReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface CustomPurchaseRepository {

    List<Purchase> getAllPurchases(String purchaseNo, String pInvoiceNo, String pOrderRefNo);

    Page<Purchase> getAllPurchasesWithPage(String purchaseNo, String pInvoiceNo, String pOrderRefNo, Pageable pageable);

    Purchase findLastPurchaseItemDetailsByPartNo(String partNo);

    List<PurchaseReport> getPurchaseReport(Date fromDate, Date toDate);
}
