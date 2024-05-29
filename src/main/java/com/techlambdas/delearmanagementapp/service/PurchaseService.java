package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.model.Purchase;
import com.techlambdas.delearmanagementapp.request.PurchaseRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PurchaseService {
    Purchase createPurchase(PurchaseRequest purchaseRequest);

    List<Purchase> getAllPurchases(String purchaseNo, String pInvoiceNo, String pOrderRefNo);

    Purchase updatePurchase(String purchaseNo, PurchaseRequest purchaseRequest);

    Page<Purchase> getAllPurchasesWithPage(String purchaseNo, String pInvoiceNo, String pOrderRefNo, Pageable pageable);
}
