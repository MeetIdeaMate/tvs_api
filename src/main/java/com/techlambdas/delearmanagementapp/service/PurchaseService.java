package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.model.ItemDetail;
import com.techlambdas.delearmanagementapp.model.Purchase;
import com.techlambdas.delearmanagementapp.request.PurchaseRequest;
import com.techlambdas.delearmanagementapp.response.ItemDetailResponse;
import com.techlambdas.delearmanagementapp.response.ItemDetailsWithPartNoResponse;
import com.techlambdas.delearmanagementapp.response.PurchaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PurchaseService {
    Purchase createPurchase(PurchaseRequest purchaseRequest);

    List<PurchaseResponse> getAllPurchases(String purchaseNo, String pInvoiceNo, String pOrderRefNo);

    Purchase updatePurchase(String purchaseNo, PurchaseRequest purchaseRequest);

    Page<Purchase> getAllPurchasesWithPage(String purchaseNo, String pInvoiceNo, String pOrderRefNo, Pageable pageable);

    ItemDetail getItemDetailsByPartNo(String partNo);
}
