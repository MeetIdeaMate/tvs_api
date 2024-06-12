package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CustomPurchaseRepository {
    List<Purchase> getAllPurchases(String purchaseNo, String pInvoiceNo, String pOrderRefNo,LocalDate fromDate,LocalDate toDate);

    Page<Purchase> getAllPurchasesWithPage(String purchaseNo, String pInvoiceNo, String pOrderRefNo, Pageable pageable, LocalDate fromDate,LocalDate toDate,String categoryName);

    Purchase findLastPurchaseItemDetailsByPartNo(String partNo);
}
