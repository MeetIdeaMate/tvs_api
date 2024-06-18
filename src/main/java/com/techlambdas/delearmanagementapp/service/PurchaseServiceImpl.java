package com.techlambdas.delearmanagementapp.service;
import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.CommonMapper;
import com.techlambdas.delearmanagementapp.mapper.PurchaseMapper;
import com.techlambdas.delearmanagementapp.model.*;
import com.techlambdas.delearmanagementapp.repository.CategoryRepository;
import com.techlambdas.delearmanagementapp.repository.PurchaseRepository;
import com.techlambdas.delearmanagementapp.request.ItemDetailRequest;
import com.techlambdas.delearmanagementapp.request.ItemRequest;
import com.techlambdas.delearmanagementapp.request.PurchaseRequest;
import com.techlambdas.delearmanagementapp.response.ItemDetailResponse;
import com.techlambdas.delearmanagementapp.response.PurchaseReport;
import com.techlambdas.delearmanagementapp.response.PurchaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.techlambdas.delearmanagementapp.repository.CustomPurchaseRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


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
    private ItemService itemService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public PurchaseResponse createPurchase(PurchaseRequest purchaseRequest) {
        try {
            Purchase purchase = purchaseMapper.mapPurchaseRequestToPurchase(purchaseRequest);


            double totalGstAmount = 0;
            double totalInvoiceAmount = 0;
            double totalIncentiveAmount = 0;
            double totalTaxAmount = 0;
            double finalInvoiceAmount = 0;

            List<ItemDetail> allItemDetails = new ArrayList<>();

            for (ItemDetailRequest itemDetailRequest : purchaseRequest.getItemDetails()) {
                List<ItemDetail> itemDetails=new ArrayList<>();
                if (Optional.ofNullable(itemDetailRequest.getMainSpecValues()).isPresent()) {
                    itemDetails = purchaseMapper.mapItemDetailRequestToItemDetailsMainSpecPresent(itemDetailRequest);
                }else {
                    ItemDetail itemDetail = purchaseMapper.mapItemDetailRequestToItemDetailsWithoutMainSpec(itemDetailRequest);
                    itemDetails.add(itemDetail);
                }
                for (ItemDetail itemDetail: itemDetails) {
                    double itemTotalValue = itemDetailRequest.getUnitRate();

                    double gstAmount = calculateTotalGstAmount(itemDetail);
                    double taxAmount = calculateTotalTaxAmount(itemDetail);
                    double incentiveAmount= calculateTotalIncentiveAmount(itemDetail);
                    if (Optional.ofNullable(itemDetailRequest.getMainSpecValues()).isPresent()) {
                        double discountAmount=itemDetailRequest.getDiscount()/itemDetailRequest.getQuantity();
                        itemDetailRequest.setDiscount(discountAmount);
                        taxAmount=taxAmount/itemDetailRequest.getQuantity();
                        incentiveAmount=incentiveAmount/itemDetailRequest.getQuantity();
                    }
                    itemDetail.setTaxableValue(itemTotalValue);
                    itemDetail.setInvoiceValue(itemTotalValue + gstAmount);
                    itemDetail.setFinalInvoiceValue((itemTotalValue + gstAmount+taxAmount)-incentiveAmount);
                    itemDetail.setGstDetails(itemDetailRequest.getGstDetails());
                    itemDetail.setIncentives(itemDetailRequest.getIncentives());
          //        updateItemDetailWithCalculations(itemDetail, itemDetailRequest);
                    totalGstAmount += gstAmount;
                    totalTaxAmount += taxAmount;
                    finalInvoiceAmount +=itemDetail.getFinalInvoiceValue();
                    totalIncentiveAmount += incentiveAmount;
                    totalInvoiceAmount += itemDetail.getInvoiceValue();
                    allItemDetails.add(itemDetail);
                }

            }
            purchase.setTotalGstAmount(totalGstAmount);
            purchase.setTotalInvoiceAmount(totalInvoiceAmount);
            purchase.setTotalValue(totalInvoiceAmount-totalGstAmount);
            purchase.setTotalIncentiveAmount(totalIncentiveAmount);
            purchase.setTotalTaxAmount(totalTaxAmount);
            purchase.setItemDetails(allItemDetails);
            purchase.setFinalTotalInvoiceAmount(finalInvoiceAmount);
            updateItemRepository(purchaseRequest.getItemDetails());
            purchase.setPurchaseNo(configService.getNextPurchaseNoSequence());
            Purchase createdPurchase= purchaseRepository.save(purchase);
            return mapPurchaseToPurchaseResponse(createdPurchase);
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex);
        }
    }

//    private void updateItemDetailWithCalculations(ItemDetail itemDetail, ItemDetailRequest itemDetailRequest) {
//        double itemTotalValue = itemDetailRequest.getUnitRate();
//        double gstAmount = calculateTotalGstAmount(itemDetailRequest);
//        double taxAmount = calculateTotalTaxAmount(itemDetailRequest);
//        double totalIncentiveAmount= calculateTotalIncentiveAmount(itemDetailRequest);
//        itemDetail.setTaxableValue(itemTotalValue);
//        itemDetail.setInvoiceValue(itemTotalValue + gstAmount);
//        itemDetail.setFinalInvoiceValue(itemTotalValue + gstAmount+taxAmount+totalIncentiveAmount);
//        itemDetail.setGstDetails(itemDetailRequest.getGstDetails());
//        itemDetail.setIncentives(itemDetailRequest.getIncentives());
//    }

    private double calculateTotalGstAmount(ItemDetail itemDetail) {
        double totalGstAmount = 0;
        double itemTotalValue = itemDetail.getUnitRate();
        if (Optional.ofNullable(itemDetail.getGstDetails()).isPresent()) {
            for (GstDetail gstDetail : itemDetail.getGstDetails()) {
                double itemGstAmount = itemTotalValue * (gstDetail.getPercentage() / 100);
                gstDetail.setGstAmount(itemGstAmount);
                totalGstAmount += itemGstAmount;
            }
        }
        return totalGstAmount;
    }

    private double calculateTotalTaxAmount(ItemDetail itemDetail) {
        double totalTaxAmount = 0;
        double itemTotalValue = itemDetail.getUnitRate();
        if (Optional.ofNullable(itemDetail.getTaxes()).isPresent()) {
            for (Taxes tax : itemDetail.getTaxes()) {
                double itemTaxAmount = itemTotalValue * (tax.getPercentage() / 100);
                tax.setTaxAmount(itemTaxAmount);
                totalTaxAmount += itemTaxAmount;
            }
        }
        return totalTaxAmount;
    }

    private double calculateTotalIncentiveAmount(ItemDetail itemDetail) {
        double totalIncentiveAmount = 0;
        double itemTotalValue = itemDetail.getUnitRate();
        if (Optional.ofNullable(itemDetail.getIncentives()).isPresent()) {
            for (Incentive incentive : itemDetail.getIncentives()) {
                double itemIncentiveAmount=0;
                if (incentive.getIncentiveAmount()>0){
                    itemIncentiveAmount=incentive.getIncentiveAmount();
                }else {
                    itemIncentiveAmount = itemTotalValue * (incentive.getPercentage() / 100);
                }
                incentive.setIncentiveAmount(itemIncentiveAmount);
                totalIncentiveAmount += itemIncentiveAmount;
            }
        }
        return totalIncentiveAmount;
    }

    private void updateItemRepository(List<ItemDetailRequest> itemDetails) {
        for (ItemDetailRequest itemDetail : itemDetails) {
            Item existingItem = itemService.findByPartNo(itemDetail.getPartNo());
            if (existingItem == null) {
                ItemRequest newItem = new ItemRequest();
                newItem.setCategoryId(itemDetail.getCategoryId());
                if (Optional.ofNullable(itemDetail.getIncentives()).isPresent()) {
                    if (!itemDetail.getIncentives().isEmpty())
                     newItem.setIncentive(true);

                }
                 newItem.setItemName(itemDetail.getItemName());
                 newItem.setPartNo(itemDetail.getPartNo());
                if (Optional.ofNullable(itemDetail.getTaxes()).isPresent()){
                    if(!itemDetail.getTaxes().isEmpty())
                        newItem.setTaxable(true);
                    }
                itemService.createItem(newItem);
            }
        }
    }
    @Override
    public List<PurchaseResponse> getAllPurchases(String purchaseNo, String pInvoiceNo, String pOrderRefNo,LocalDate fromDate,LocalDate toDate,String categoryName,String branchId) {
        List<Purchase> purchases = customPurchaseRepository.getAllPurchases(purchaseNo, pInvoiceNo, pOrderRefNo,fromDate,toDate,categoryName,branchId);
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
      //      purchaseMapper.updatePurchaseFromRequest(purchaseRequest, existingPurchase);
            return purchaseRepository.save(existingPurchase);
        } catch (DataNotFoundException ex) {
            throw new DataNotFoundException("Data not found --" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public Page<PurchaseResponse> getAllPurchasesWithPage(String purchaseNo, String pInvoiceNo, String pOrderRefNo, Pageable pageable, LocalDate fromDate,LocalDate toDate,String categoryName,String branchId) {
      Page<Purchase>purchases= customPurchaseRepository.getAllPurchasesWithPage(purchaseNo, pInvoiceNo, pOrderRefNo, pageable,fromDate,toDate,categoryName,branchId);
      List<PurchaseResponse> purchaseResponses=mapEntityWithResponse(purchases.getContent());
      return new PageImpl<>(purchaseResponses,pageable,purchases.getTotalElements());
    }

    private List<PurchaseResponse> mapEntityWithResponse(List<Purchase> purchases) {
        List<PurchaseResponse>purchaseResponses=new ArrayList<>();
        for (Purchase purchase:purchases){
            PurchaseResponse purchaseResponse=mapPurchaseToPurchaseResponse(purchase);
            purchaseResponses.add(purchaseResponse);
        }
        return purchaseResponses;
    }

    private PurchaseResponse mapPurchaseToPurchaseResponse(Purchase purchase) {
        PurchaseResponse purchaseResponse=commonMapper.toPurchaseResponse(purchase);
        Map<String, List<ItemDetail>> itemDetailGroupedByPartNo =purchase.getItemDetails().stream()
                .collect(Collectors.groupingBy(ItemDetail::getPartNo));
        List<ItemDetailResponse> itemDetailResponses = new ArrayList<>();
        for (Map.Entry<String, List<ItemDetail>> entry : itemDetailGroupedByPartNo.entrySet()) {
            itemDetailResponses.add(mapItemDetailToItemDetailResponse(entry.getValue()));
        }
        purchaseResponse.setItemDetails(itemDetailResponses);
        return purchaseResponse;
    }
    private ItemDetailResponse mapItemDetailToItemDetailResponse(List<ItemDetail> itemDetails) {
        ItemDetail itemDetail=itemDetails.get(0);
        int quantity=itemDetails.size();
        List<Map<String, String>> mainSpecValues = itemDetails.stream()
                .map(it -> it.getMainSpecValue())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        ItemDetailResponse itemDetailResponse =commonMapper.toItemDetailResponse(itemDetail);
        double itemTotalValue=quantity*itemDetail.getUnitRate();
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
        itemDetailResponse.setMainSpecValues(mainSpecValues);
        itemDetailResponse.setValue(quantity*itemDetail.getUnitRate());
        itemDetailResponse.setQuantity(quantity);
        return itemDetailResponse;
    }


    @Override
    public ItemDetailResponse getItemDetailsByPartNo(String partNo) {
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
            ItemDetailResponse itemDetailResponse =commonMapper.toItemDetailResponse(existingItem);
        return itemDetailResponse;
    }

    @Override
    public String cancelPurchase(String purchaseId) {
        Optional<Purchase> existingPurchase = purchaseRepository.findByPurchaseId(purchaseId);
            if (existingPurchase.isPresent()){
               Purchase purchase= existingPurchase.get();
               purchase.setCancelled(true);
               purchaseRepository.save(purchase);
               return "Cancelled Successfully";
            }else {
                throw new DataNotFoundException("Purchase not found this purchaseId : " + purchaseId);
            }
    }

    @Override
    public Boolean validatePurchaseItem(String partNo, Map<String, String> mainSpecValue) {
        Purchase purchase=customPurchaseRepository.findPuechaseByPartNoAndMainspecValue(partNo,mainSpecValue);
        if (Optional.ofNullable(purchase).isPresent()){
            return true;
        }
        return false;
    }



    @Override
    public List<PurchaseReport> getPurchaseReport(Date fromDate, Date toDate) {
        return customPurchaseRepository.getPurchaseReport(fromDate, toDate);
    }
}
