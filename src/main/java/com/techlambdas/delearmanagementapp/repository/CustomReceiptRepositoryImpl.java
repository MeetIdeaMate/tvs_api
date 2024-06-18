package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomReceiptRepositoryImpl implements CustomReceiptRepository{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Receipt> getAllReceipts(String receiptId, LocalDate fromDate, LocalDate toDate) {
        Query query=new Query();
        if (Optional.ofNullable(receiptId).isPresent())
            query.addCriteria(Criteria.where("receiptId").is(receiptId));
        if (fromDate !=null&& toDate !=null)
        {
            query.addCriteria(Criteria.where("receiptDate").gte(fromDate).lte(toDate));
        }
        return mongoTemplate.find(query,Receipt.class);
    }

    @Override
    public Page<Receipt> getAllReceiptsWithPage(String receiptId, LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        Query query=new Query();
        if (Optional.ofNullable(receiptId).isPresent())
            query.addCriteria(Criteria.where("receiptId").is(receiptId));
        if (fromDate !=null&& toDate !=null)
        {
            query.addCriteria(Criteria.where("receiptDate").gte(fromDate).lte(toDate));
        }
        long count=mongoTemplate.count(query,Receipt.class);
        query.with(pageable);
        List<Receipt> receipts=mongoTemplate.find(query,Receipt.class);
        return new PageImpl<>(receipts,pageable,count);
    }
}
