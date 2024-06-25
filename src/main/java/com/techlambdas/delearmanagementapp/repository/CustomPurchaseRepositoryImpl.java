package com.techlambdas.delearmanagementapp.repository;

import com.mongodb.client.MongoCollection;
import com.techlambdas.delearmanagementapp.model.Category;
import com.techlambdas.delearmanagementapp.model.Purchase;
import com.techlambdas.delearmanagementapp.response.PurchaseReport;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Locale;

@Repository
public class CustomPurchaseRepositoryImpl implements CustomPurchaseRepository {
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public List<Purchase> getAllPurchases(String purchaseNo, String pInvoiceNo, String pOrderRefNo,LocalDate fromDate,LocalDate toDate,String categoryName,String branchId,Boolean isStockUpdated) {
        Query query=new Query();
        if (Optional.ofNullable(branchId).isPresent()){
            query.addCriteria(Criteria.where("branchId").is(branchId));
        }
        if (fromDate != null && toDate != null) {
            query.addCriteria(Criteria.where("p_invoiceDate").gte(fromDate).lte(toDate));
        }
        if (purchaseNo!=null)
            query.addCriteria(Criteria.where("purchaseNo").is(purchaseNo));
        if (isStockUpdated!=null)
            query.addCriteria(Criteria.where("isStockUpdated").in(isStockUpdated));
        if (pInvoiceNo!=null)
            query.addCriteria(Criteria.where("pInvoiceNo").regex(pInvoiceNo));
        if (Optional.ofNullable(categoryName).isPresent()) {
            List<String> categoryIdList= getCategoryNameFromCategory(categoryName);
            query.addCriteria(Criteria.where("itemDetails.categoryId").in(categoryIdList));
        }
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));
        query.addCriteria(Criteria.where("isCancelled").is(false));
        if (pOrderRefNo!=null)
            query.addCriteria(Criteria.where("pInvoiceNo").regex(pOrderRefNo));
        return mongoTemplate.find(query, Purchase.class);
    }

    @Override
    public Page<Purchase> getAllPurchasesWithPage(String purchaseNo, String pInvoiceNo, String pOrderRefNo, Pageable pageable, LocalDate fromDate, LocalDate toDate,String categoryName,String branchId,Boolean isStockUpdated) {
        Query query=new Query();
        if (Optional.ofNullable(branchId).isPresent()){
            query.addCriteria(Criteria.where("branchId").is(branchId));
        }
        if (fromDate != null && toDate != null) {
            query.addCriteria(Criteria.where("p_invoiceDate").gte(fromDate).lte(toDate));
        }
        if (Optional.ofNullable(categoryName).isPresent()) {
            List<String> categoryIdList= getCategoryNameFromCategory(categoryName);
            query.addCriteria(Criteria.where("itemDetails.categoryId").in(categoryIdList));
        }
        query.addCriteria(Criteria.where("isCancelled").is(false));

        if (purchaseNo!=null)
            query.addCriteria(Criteria.where("purchaseNo").is(purchaseNo));
        if (pInvoiceNo!=null)
            query.addCriteria(Criteria.where("p_invoiceNo").regex(pInvoiceNo));
        if (pOrderRefNo!=null)
            query.addCriteria(Criteria.where("p_orderRefNo").regex(pOrderRefNo));
        if (isStockUpdated!=null)
            query.addCriteria(Criteria.where("isStockUpdated").in(isStockUpdated));
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));
        long count = mongoTemplate.count(query, Purchase.class);
        query.with(pageable);
        List<Purchase> purchases = mongoTemplate.find(query, Purchase.class);

        return new PageImpl<>(purchases, pageable, count);
    }
    public List<String>getCategoryNameFromCategory(String categoryName)
    {
        Query query=new Query();
        if (categoryName!=null){
            query.addCriteria(Criteria.where("categoryName").regex("^.*"+categoryName+".*","i"));
        }
        List<Category> categoryList=mongoTemplate.find(query, Category.class);
        return categoryList.stream().map(Category::getCategoryId).collect(Collectors.toList());
    }

    @Override
    public Purchase findLastPurchaseItemDetailsByPartNo(String partNo) {
        Query query = new Query()
                .addCriteria(Criteria.where("itemDetails.partNo").is(partNo))
                .with(Sort.by(Sort.Order.desc("p_invoiceDate")))
                .limit(1);
        return mongoTemplate.findOne(query, Purchase.class);

    }

    @Override
    public Purchase findPuechaseByPartNoAndMainspecValue(String partNo, Map<String, String> mainSpecValue) {
        Query query = new Query();
        query.addCriteria(Criteria.where("itemDetails.partNo").is(partNo));
        query.addCriteria(Criteria.where("itemDetails.mainSpecValue").is(mainSpecValue));
        return mongoTemplate.findOne(query, Purchase.class);
    }

    @Override
    public List<PurchaseReport> getPurchaseReport(Date fromDate, Date toDate) {
        List<Criteria> criteriaList = new ArrayList<>();
        if (fromDate != null) {
            criteriaList.add(Criteria.where("p_invoiceDate").gte(fromDate));
        }
        if (toDate != null) {
            criteriaList.add(Criteria.where("p_invoiceDate").lte(toDate));
        }

        Criteria criteria = new Criteria();
        if (!criteriaList.isEmpty()) {
            criteria.andOperator(criteriaList.toArray(new Criteria[0]));
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.unwind("itemDetails",true),
                Aggregation.group("itemDetails.partNo")
                        .sum("itemDetails.quantity").as("quantity")
                        .sum("itemDetails.invoiceValue").as("totalInvoiceValue")
                        .sum("itemDetails.finalInvoiceValue").as("finalInvoiceValue")
                        .sum("itemDetails.incentives.incentiveAmount").as("totalIncentive")
                        .first("itemDetails.itemName").as("itemName")
                        .first("itemDetails.specificationsValue.variant").as("variant")
                        .first("itemDetails.hsnSacCode").as("hsnSacCode")
                        .first("itemDetails.unitRate").as("unitRate")
                        .first("itemDetails.taxableValue").as("taxableValue")
                        .first("itemDetails.gstDetails").as("gstDetail")
                        .first("itemDetails.incentives").as("incentives")
        );

        AggregationResults<PurchaseReport> results = mongoTemplate.aggregate(aggregation, Purchase.class, PurchaseReport.class);
        List<PurchaseReport> purchaseReportList = results.getMappedResults();

        double overallAmount = purchaseReportList.stream()
                .mapToDouble(PurchaseReport::getFinalInvoiceValue)
                .sum();

        purchaseReportList.forEach(report -> report.setOverallAmount(overallAmount));

        return purchaseReportList;
//        Query query = new Query();
//        if (fromDate != null || toDate != null) {
//            List<Criteria> criteriaList = new ArrayList<>();
//
//            if (fromDate != null) {
//                criteriaList.add(Criteria.where("p_invoiceDate").gte(fromDate));
//            }
//
//            if (toDate != null) {
//                criteriaList.add(Criteria.where("p_invoiceDate").lte(toDate));
//            }
//
//            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
//        }
//        List<Purchase> purchases = mongoTemplate.find(query, Purchase.class);
//        List<PurchaseReport> purchaseReports = new ArrayList<>();
//        double overallAmount = 0;
//
//        for (Purchase purchase : purchases) {
//            PurchaseReport report = new PurchaseReport();
//            report.setItemName(purchase.getItemDetails().get(0).getItemName());
//            report.setVariant(purchase.getItemDetails().get(0).getSpecificationsValue().get("variant"));
//            report.setHsnSacCode(purchase.getItemDetails().get(0).getCategoryId());
//            report.setQuantity(purchase.getItemDetails().get(0).getQuantity());
//            report.setUniteRate(purchase.getItemDetails().get(0).getUnitRate());
//            report.setTaxableValue(purchase.getItemDetails().get(0).getTaxableValue());
//            report.setGstDetail(purchase.getItemDetails().get(0).getGstDetails());
//            report.setTotalInvoiceValue(purchase.getItemDetails().get(0).getInvoiceValue());
//            report.setIncentives(purchase.getItemDetails().get(0).getIncentives());
//            report.setTotalIncentive(0);
//            report.setFinalInvoiceValue(purchase.getItemDetails().get(0).getFinalInvoiceValue());
//
//            purchaseReports.add(report);
//
//            overallAmount += purchase.getFinalTotalInvoiceAmount();
//        }
//        System.out.println("Overall Amount: " + NumberFormat.getNumberInstance(Locale.US).format(overallAmount));
//        return purchaseReports;
    }
}
