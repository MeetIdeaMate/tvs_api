package com.techlambdas.delearmanagementapp.service;
import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.CommonMapper;
import com.techlambdas.delearmanagementapp.mapper.PurchaseMapper;
import com.techlambdas.delearmanagementapp.model.*;
import com.techlambdas.delearmanagementapp.repository.ItemRepository;
import com.techlambdas.delearmanagementapp.repository.PurchaseRepository;
import com.techlambdas.delearmanagementapp.request.ItemDetailRequest;
import com.techlambdas.delearmanagementapp.request.PurchaseRequest;
import com.techlambdas.delearmanagementapp.response.ItemDetailResponse;
import com.techlambdas.delearmanagementapp.response.PurchaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.techlambdas.delearmanagementapp.repository.CustomPurchaseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private PurchaseMapper purchaseMapper;
    @Autowired
    private CustomPurchaseRepository customPurchaseRepository;
    @Autowired
    private CommonMapper commonMapper;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ConfigService configService;
    @Override
    public Purchase createPurchase(PurchaseRequest purchaseRequest) {
        try {
            Purchase purchase = purchaseMapper.mapPurchaseRequestToPurchase(purchaseRequest);
            purchase.setPurchaseNo(configService.getNextPurchaseNoSequence());

            double totalGstAmount = 0;
            double totalInvoiceAmount = 0;
            double totalIncentiveAmount = 0;
            double totalTaxAmount = 0;

            List<ItemDetail> allItemDetails = new ArrayList<>();

            for (ItemDetailRequest itemDetailRequest : purchaseRequest.getItemDetails()) {
                List<ItemDetail> itemDetails = purchaseMapper.mapItemDetailRequestToItemDetails(itemDetailRequest);
                for (ItemDetail itemDetail : itemDetails) {
                    updateItemDetailWithCalculations(itemDetail, itemDetailRequest);
                    totalGstAmount += calculateTotalGstAmount(itemDetailRequest);
                    totalTaxAmount += calculateTotalTaxAmount(itemDetailRequest);
                    totalIncentiveAmount += calculateTotalIncentiveAmount(itemDetailRequest);
                    totalInvoiceAmount += itemDetail.getInvoiceValue();
                    allItemDetails.add(itemDetail);
                }
            }
            purchase.setTotalGstAmount(totalGstAmount);
            purchase.setTotalInvoiceAmount(totalInvoiceAmount);
            purchase.setTotalIncentiveAmount(totalIncentiveAmount);
            purchase.setTotalTaxAmount(totalTaxAmount);
            purchase.setItemDetails(allItemDetails);

            updateItemRepository(purchaseRequest.getItemDetails());

            return purchaseRepository.save(purchase);
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex);
        }
    }
    private void updateItemDetailWithCalculations(ItemDetail itemDetail, ItemDetailRequest itemDetailRequest) {
        double itemTotalValue = itemDetailRequest.getUnitRate();
        double gstAmount = calculateTotalGstAmount(itemDetailRequest);
        double taxAmount = calculateTotalTaxAmount(itemDetailRequest);

        itemDetail.setTaxableValue(itemTotalValue);
        itemDetail.setInvoiceValue(itemTotalValue + gstAmount + taxAmount);
        itemDetail.setGstDetails(itemDetailRequest.getGstDetails());
        itemDetail.setIncentives(itemDetailRequest.getIncentives());
    }

    private double calculateTotalGstAmount(ItemDetailRequest itemDetailRequest) {
        double totalGstAmount = 0;
        double itemTotalValue = itemDetailRequest.getUnitRate();
        for (GstDetail gstDetail : itemDetailRequest.getGstDetails()) {
            double itemGstAmount = itemTotalValue * (gstDetail.getPercentage() / 100);
            gstDetail.setGstAmount(itemGstAmount);
            totalGstAmount += itemGstAmount;
        }
        return totalGstAmount;
    }

    private double calculateTotalTaxAmount(ItemDetailRequest itemDetailRequest) {
        double totalTaxAmount = 0;
        double itemTotalValue = itemDetailRequest.getUnitRate();
        for (Taxes tax : itemDetailRequest.getTaxes()) {
            double itemTaxAmount = itemTotalValue * (tax.getPercentage() / 100);
            tax.setTaxAmount(itemTaxAmount);
            totalTaxAmount += itemTaxAmount;
        }
        return totalTaxAmount;
    }

    private double calculateTotalIncentiveAmount(ItemDetailRequest itemDetailRequest) {
        double totalIncentiveAmount = 0;
        double itemTotalValue = itemDetailRequest.getUnitRate();
        for (Incentive incentive : itemDetailRequest.getIncentives()) {
            double itemIncentiveAmount = itemTotalValue * (incentive.getPercentage() / 100);
            incentive.setIncentiveAmount(itemIncentiveAmount);
            totalIncentiveAmount += itemIncentiveAmount;
        }
        return totalIncentiveAmount;
    }

    private void updateItemRepository(List<ItemDetailRequest> itemDetails) {
        for (ItemDetailRequest itemDetail : itemDetails) {
            Item existingItem = itemRepository.findByPartNo(itemDetail.getPartNo());
            if (!Optional.of(existingItem).isPresent()) {
                Item newItem = new Item();
                newItem.setCategoryId(itemDetail.getCategoryId());
                if (Optional.of(itemDetail.getFinalInvoiceValue()).isPresent()&&!itemDetail.getIncentives().isEmpty())
                 newItem.setIncentive(true);
                 newItem.setItemName(itemDetail.getItemName());
                 newItem.setPartNo(itemDetail.getPartNo());
                if (Optional.of(itemDetail.getTaxes()).isPresent()&&!itemDetail.getTaxes().isEmpty())
                    newItem.setTaxable(true);
                itemRepository.save(newItem);
            }
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
    public Page<PurchaseResponse> getAllPurchasesWithPage(String purchaseNo, String pInvoiceNo, String pOrderRefNo, Pageable pageable) {
      Page<Purchase>purchases= customPurchaseRepository.getAllPurchasesWithPage(purchaseNo, pInvoiceNo, pOrderRefNo, pageable);
      List<PurchaseResponse> purchaseResponses=mapEntityWithResponse(purchases.getContent());
      return new PageImpl<>(purchaseResponses,pageable,purchases.getTotalElements());
    }

    private List<PurchaseResponse> mapEntityWithResponse(List<Purchase> purchases) {
        List<PurchaseResponse>purchaseResponses=new ArrayList<>();
        for (Purchase purchase:purchases){
            PurchaseResponse purchaseResponse=mapPurchaseToPurchaseResponse(purchase);
        }
        return purchaseResponses;
    }

    private PurchaseResponse mapPurchaseToPurchaseResponse(Purchase purchase) {
        PurchaseResponse purchaseResponse=purchaseMapper.mapEntityWithResponse(purchase);
        Map<String, List<ItemDetail>> itemDetailGroupedByPartNo = purchase.getItemDetails().stream()
                .collect(Collectors.groupingBy(ItemDetail::getPartNo));
        List<ItemDetailResponse> itemDetailResponses = new ArrayList<>();
        for (Map.Entry<String, List<ItemDetail>> entry : itemDetailGroupedByPartNo.entrySet()) {
            itemDetailResponses.add(mapItemDetailsGroupToItemDetailResponses(entry.getKey(), entry.getValue()));
        }
        purchaseResponse.setItemDetails(itemDetailResponses);

        return purchaseResponse;
    }
    private ItemDetailResponse mapItemDetailsGroupToItemDetailResponses(String partNo, List<ItemDetail> itemDetails) {

        ItemDetailResponse itemDetailResponse = new ItemDetailResponse();

        itemDetailResponse.setPartNo(partNo);

        itemDetailResponse = mapItemDetailToItemDetailResponse(itemDetails);

        return itemDetailResponse;
    }
    private ItemDetailResponse mapItemDetailToItemDetailResponse(List<ItemDetail> itemDetails) {
        ItemDetail itemDetail=itemDetails.get(0);
        ItemDetailResponse itemDetailResponse =purchaseMapper.mapItemDetailResponseWithItemDetail(itemDetail);
        double itemTotalValue=itemDetails.size()*itemDetail.getUnitRate();
        double gstAmount=0;
        if (Optional.of(itemDetailResponse.getGstDetails()).isPresent()) {
            for (GstDetail gstDetail : itemDetailResponse.getGstDetails()) {
                double itemGstAmount = itemTotalValue * (gstDetail.getPercentage() / 100);
                gstDetail.setGstAmount(itemGstAmount);
                gstAmount += itemGstAmount;
            }
        }
        double incentiveAmount=0;
        if (Optional.of(itemDetailResponse.getIncentives()).isPresent()) {
        for (Incentive incentive:itemDetailResponse.getIncentives())
        {
            double itemIncentiveAmount = itemTotalValue * (incentive.getPercentage() / 100);
            incentive.setIncentiveAmount(itemIncentiveAmount);
            incentiveAmount +=itemIncentiveAmount;
        }
        }
        double taxAmount=0;
        if (Optional.of(itemDetailResponse.getTaxes()).isPresent()) {
            for (Taxes taxes : itemDetailResponse.getTaxes()) {
                double itemTaxAmount = itemTotalValue * (taxes.getPercentage() / 100);
                taxes.setTaxAmount(itemTaxAmount);
                taxAmount += itemTaxAmount;
            }
        }
        itemDetailResponse.setTaxableValue(itemTotalValue);
        itemDetailResponse.setInvoiceValue(itemTotalValue + gstAmount);
        itemDetailResponse.setFinalInvoiceValue(itemTotalValue+gstAmount+incentiveAmount+taxAmount);
        return itemDetailResponse;
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
