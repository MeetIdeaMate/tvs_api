package com.techlambdas.delearmanagementapp.repository;

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

import java.util.List;

@Repository
public class CustomPurchaseRepositoryImpl implements CustomPurchaseRepository{
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public List<Purchase> getAllPurchases(String purchaseNo, String pInvoiceNo, String pOrderRefNo) {
        Query query=new Query();
        if (purchaseNo!=null)
            query.addCriteria(Criteria.where("purchaseNo").is(purchaseNo));
        if (pInvoiceNo!=null)
            query.addCriteria(Criteria.where("pInvoiceNo").regex(pInvoiceNo));
        if (pOrderRefNo!=null)
            query.addCriteria(Criteria.where("pInvoiceNo").regex(pOrderRefNo));
        return mongoTemplate.find(query, Purchase.class);
    }

    @Override
    public Page<Purchase> getAllPurchasesWithPage(String purchaseNo, String pInvoiceNo, String pOrderRefNo, Pageable pageable) {
        Query query=new Query();
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
}
