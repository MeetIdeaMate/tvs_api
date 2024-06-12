package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Category;
import com.techlambdas.delearmanagementapp.model.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CustomPurchaseRepositoryImpl implements CustomPurchaseRepository{
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public List<Purchase> getAllPurchases(String purchaseNo, String pInvoiceNo, String pOrderRefNo,LocalDate fromDate,LocalDate toDate,String categoryName,String branchId) {
        Query query=new Query();
        if (Optional.ofNullable(branchId).isPresent()){
            query.addCriteria(Criteria.where("branchId").is(branchId));
        }
        if (fromDate != null && toDate != null) {
            query.addCriteria(Criteria.where("p_invoiceDate").gte(fromDate).lte(toDate));
        }
        if (purchaseNo!=null)
            query.addCriteria(Criteria.where("purchaseNo").is(purchaseNo));
        if (pInvoiceNo!=null)
            query.addCriteria(Criteria.where("pInvoiceNo").regex(pInvoiceNo));
        if (Optional.ofNullable(categoryName).isPresent()) {
            List<String> categoryIdList= getCategoryNameFromCategory(categoryName);
            query.addCriteria(Criteria.where("itemDetails.categoryId").in(categoryIdList));
        }
        query.addCriteria(Criteria.where("isCancelled").is(false));
        if (pOrderRefNo!=null)
            query.addCriteria(Criteria.where("pInvoiceNo").regex(pOrderRefNo));
        return mongoTemplate.find(query, Purchase.class);
    }

    @Override
    public Page<Purchase> getAllPurchasesWithPage(String purchaseNo, String pInvoiceNo, String pOrderRefNo, Pageable pageable, LocalDate fromDate, LocalDate toDate,String categoryName,String branchId) {
        Query query=new Query();
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
}
