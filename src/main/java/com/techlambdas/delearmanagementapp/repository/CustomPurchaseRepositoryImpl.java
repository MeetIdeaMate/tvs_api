package com.techlambdas.delearmanagementapp.repository;

import com.mongodb.client.MongoCollection;
import com.techlambdas.delearmanagementapp.model.Purchase;
import com.techlambdas.delearmanagementapp.response.PurchaseReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Repository
public class CustomPurchaseRepositoryImpl implements CustomPurchaseRepository {
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public List<Purchase> getAllPurchases(String purchaseNo, String pInvoiceNo, String pOrderRefNo,LocalDate fromDate,LocalDate toDate) {
        Query query=new Query();
        if (fromDate != null && toDate != null) {
            query.addCriteria(Criteria.where("p_invoiceDate").gte(fromDate).lte(toDate));
        }
        if (purchaseNo!=null)
            query.addCriteria(Criteria.where("purchaseNo").is(purchaseNo));
        if (pInvoiceNo!=null)
            query.addCriteria(Criteria.where("pInvoiceNo").regex(pInvoiceNo));
        if (pOrderRefNo!=null)
            query.addCriteria(Criteria.where("pInvoiceNo").regex(pOrderRefNo));
        return mongoTemplate.find(query, Purchase.class);
    }

        @Override
        public Page<Purchase> getAllPurchasesWithPage(String purchaseNo, String pInvoiceNo, String pOrderRefNo, Pageable pageable, LocalDate fromDate, LocalDate toDate) {
            Query query=new Query();
            if (fromDate != null && toDate != null) {
                query.addCriteria(Criteria.where("p_invoiceDate").gte(fromDate).lte(toDate));
            }
            if (purchaseNo!=null)
                query.addCriteria(Criteria.where("purchaseNo").is(purchaseNo));
            if (pInvoiceNo!=null)
                query.addCriteria(Criteria.where("p_invoiceNo").regex(pInvoiceNo));
            if (pOrderRefNo!=null)
                query.addCriteria(Criteria.where("p_orderRefNo").regex(pOrderRefNo));
            long count = mongoTemplate.count(query, Purchase.class);
            query.with(pageable);
            List<Purchase> purchases = mongoTemplate.find(query, Purchase.class);

        return new PageImpl<>(purchases, pageable, count);
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
    public List<PurchaseReport> getPurchaseReport(Date fromDate, Date toDate) {
        Query query = new Query();
        if (fromDate != null || toDate != null) {
            List<Criteria> criteriaList = new ArrayList<>();

            if (fromDate != null) {
                criteriaList.add(Criteria.where("p_invoiceDate").gte(fromDate));
            }

            if (toDate != null) {
                criteriaList.add(Criteria.where("p_invoiceDate").lte(toDate));
            }

            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }
        List<Purchase> purchases = mongoTemplate.find(query, Purchase.class);
        List<PurchaseReport> purchaseReports = new ArrayList<>();
        double overallAmount = 0;

        for (Purchase purchase : purchases) {
            PurchaseReport report = new PurchaseReport();
            report.setItemName(purchase.getItemDetails().get(0).getItemName());
            report.setVariant(purchase.getItemDetails().get(0).getSpecificationsValue().get("variant"));
            report.setHsnSacCode(purchase.getItemDetails().get(0).getCategoryId());
            report.setQuantity(purchase.getItemDetails().get(0).getQuantity());
            report.setUniteRate(purchase.getItemDetails().get(0).getUnitRate());
            report.setTaxableValue(purchase.getItemDetails().get(0).getTaxableValue());
            report.setGstDetail(purchase.getItemDetails().get(0).getGstDetails());
            report.setTotalInvoiceValue(purchase.getItemDetails().get(0).getInvoiceValue());
            report.setIncentives(purchase.getItemDetails().get(0).getIncentives());
            report.setTotalIncentive(0);
            report.setFinalInvoiceValue(purchase.getItemDetails().get(0).getFinalInvoiceValue());

            purchaseReports.add(report);

            overallAmount += purchase.getFinalTotalInvoiceAmount();
        }
        System.out.println("Overall Amount: " + NumberFormat.getNumberInstance(Locale.US).format(overallAmount));
        return purchaseReports;
    }
}
