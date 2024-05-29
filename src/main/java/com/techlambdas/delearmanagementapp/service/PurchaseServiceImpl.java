package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.PurchaseMapper;
import com.techlambdas.delearmanagementapp.model.Purchase;
import com.techlambdas.delearmanagementapp.model.Vendor;
import com.techlambdas.delearmanagementapp.repository.PurchaseRepository;
import com.techlambdas.delearmanagementapp.request.PurchaseRequest;
import com.techlambdas.delearmanagementapp.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.techlambdas.delearmanagementapp.repository.CustomPurchaseRepository;

import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService{
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private PurchaseMapper purchaseMapper;
    @Autowired
    private CustomPurchaseRepository customPurchaseRepository;
    @Override
    public Purchase createPurchase(PurchaseRequest purchaseRequest) {
        try {
            Purchase purchase = purchaseMapper.mapPurchaseRequestToPurchase(purchaseRequest);
            purchase.setPurchaseNo(RandomIdGenerator.getRandomId());
            return purchaseRepository.save(purchase);
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public List<Purchase> getAllPurchases(String purchaseNo, String pInvoiceNo, String pOrderRefNo) {
        List<Purchase> purchases=customPurchaseRepository.getAllPurchases(purchaseNo,pInvoiceNo,pOrderRefNo);
        return purchases;
    }

    @Override
    public Purchase updatePurchase(String purchaseNo, PurchaseRequest purchaseRequest) {
        try {
            Purchase existingPurchase = purchaseRepository.findByPurchaseNo(purchaseNo);
            if (existingPurchase == null)
                throw new DataNotFoundException("purchaseNo not found with ID: " + purchaseNo);
            purchaseMapper.updatePurchaseFromRequest(purchaseRequest, existingPurchase);
            return purchaseRepository.save(existingPurchase);
        }catch (DataNotFoundException ex) {
            throw new DataNotFoundException("Data not found --" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public Page<Purchase> getAllPurchasesWithPage(String purchaseNo, String pInvoiceNo, String pOrderRefNo, Pageable pageable) {
        return customPurchaseRepository.getAllPurchasesWithPage(purchaseNo,pInvoiceNo,pOrderRefNo, pageable);
    }
}
