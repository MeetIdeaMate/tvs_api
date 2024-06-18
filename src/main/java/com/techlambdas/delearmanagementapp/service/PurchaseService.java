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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface PurchaseService {
    PurchaseResponse createPurchase(PurchaseRequest purchaseRequest);

    List<PurchaseResponse> getAllPurchases(String purchaseNo, String pInvoiceNo, String pOrderRefNo,LocalDate fromDate,LocalDate toDate,String categoryName,String branchId);

    Purchase updatePurchase(String purchaseNo, PurchaseRequest purchaseRequest);

    Page<PurchaseResponse> getAllPurchasesWithPage(String purchaseNo, String pInvoiceNo, String pOrderRefNo, Pageable pageable, LocalDate fromDate,LocalDate toDate,String categoryName,String branchId);

    ItemDetailResponse getItemDetailsByPartNo(String partNo);

    String cancelPurchase(String purchaseId);

    Boolean validatePurchaseItem(String partNo, Map<String, String> mainSpecValue);
}
