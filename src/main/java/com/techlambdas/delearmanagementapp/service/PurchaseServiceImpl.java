package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.CommonMapper;
import com.techlambdas.delearmanagementapp.mapper.PurchaseMapper;
import com.techlambdas.delearmanagementapp.model.GstDetail;
import com.techlambdas.delearmanagementapp.model.Incentive;
import com.techlambdas.delearmanagementapp.model.ItemDetail;
import com.techlambdas.delearmanagementapp.model.Purchase;
import com.techlambdas.delearmanagementapp.repository.PurchaseRepository;
import com.techlambdas.delearmanagementapp.request.PurchaseRequest;
import com.techlambdas.delearmanagementapp.response.ItemDetailsWithPartNoResponse;
import com.techlambdas.delearmanagementapp.response.PurchaseResponse;
import com.techlambdas.delearmanagementapp.utils.RandomIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.techlambdas.delearmanagementapp.repository.CustomPurchaseRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseServiceImpl.class);
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private PurchaseMapper purchaseMapper;
    @Autowired
    private CustomPurchaseRepository customPurchaseRepository;
    @Autowired
    private CommonMapper commonMapper;

    @Override
    public Purchase createPurchase(PurchaseRequest purchaseRequest) {
        try {
            Purchase purchase = purchaseMapper.mapPurchaseRequestToPurchase(purchaseRequest);
            purchase.setPurchaseNo(RandomIdGenerator.getRandomId());
            double totalGstAmount = 0;
            double totalInvoiceAmount = 0;
            double totalIncentiveAmount = 0;
            double totalTaxAmount = 0;
            for (ItemDetail itemDetail : purchase.getItemDetails()) {
                double itemTotalValue = itemDetail.getUnitRate() * itemDetail.getQuantity();
                double gstAmount = 0;
                double taxAmount=0;
                for (GstDetail gstDetail : itemDetail.getGstDetails()) {
                    double itemGstAmount = itemTotalValue * (gstDetail.getPercentage() / 100);
                    gstDetail.setGstAmount(itemGstAmount);
                    gstAmount = gstAmount + itemGstAmount;
                    double itemtaxAmount=gstDetail.getGstAmount();
                    taxAmount=taxAmount+itemtaxAmount;
                }
                double incentiveAmount = 0;
                for (Incentive incentive:itemDetail.getIncentives())
                {
                    double itemIncentiveAmount = itemTotalValue * (incentive.getPercentage() / 100);
                    incentive.setIncentiveAmount(itemIncentiveAmount);
                    incentiveAmount=incentiveAmount+itemIncentiveAmount;
                }
                itemDetail.setTaxableValue(itemTotalValue);
                itemDetail.setInvoiceValue(itemTotalValue + gstAmount);
                totalGstAmount = totalGstAmount + gstAmount;
                totalInvoiceAmount = totalInvoiceAmount + itemDetail.getInvoiceValue();
                totalIncentiveAmount=totalIncentiveAmount+incentiveAmount;
                totalTaxAmount=totalTaxAmount+taxAmount;
            }
            purchase.setTotalGstAmount(totalGstAmount);
            purchase.setTotalInvoiceAmount(totalInvoiceAmount);
            purchase.setTotalIncentiveAmount(totalIncentiveAmount);
            purchase.setTotalTaxAmount(totalTaxAmount);
            return purchaseRepository.save(purchase);
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public List<PurchaseResponse> getAllPurchases(String purchaseNo, String pInvoiceNo, String pOrderRefNo) {
        List<Purchase> purchases = customPurchaseRepository.getAllPurchases(purchaseNo, pInvoiceNo, pOrderRefNo);
        return purchases.stream()
                .map(commonMapper::toPurchaseResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Purchase updatePurchase(String purchaseNo, PurchaseRequest purchaseRequest) {
        try {
            Purchase existingPurchase = purchaseRepository.findByPurchaseNo(purchaseNo);
            if (existingPurchase == null)
                throw new DataNotFoundException("purchaseNo not found with ID: " + purchaseNo);
            purchaseMapper.updatePurchaseFromRequest(purchaseRequest, existingPurchase);
            return purchaseRepository.save(existingPurchase);
        } catch (DataNotFoundException ex) {
            throw new DataNotFoundException("Data not found --" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public Page<Purchase> getAllPurchasesWithPage(String purchaseNo, String pInvoiceNo, String pOrderRefNo, Pageable pageable) {
        return customPurchaseRepository.getAllPurchasesWithPage(purchaseNo, pInvoiceNo, pOrderRefNo, pageable);
    }

    @Override
    public ItemDetail getItemDetailsByPartNo(String partNo) {
        Purchase purchase = customPurchaseRepository.findLastPurchaseItemDetailsByPartNo(partNo);

        if (purchase == null)
            throw new DataNotFoundException("Purchase not found this partNo : " +partNo);
        ItemDetail existingItem= new ItemDetail();
            for(ItemDetail itemDetail:purchase.getItemDetails())
            {
                if (itemDetail.getPartNo().equals(partNo))
                {
                    existingItem=itemDetail;
                    existingItem.setMainSpecValue(null);
                }
            }
            return existingItem;
    }
}
