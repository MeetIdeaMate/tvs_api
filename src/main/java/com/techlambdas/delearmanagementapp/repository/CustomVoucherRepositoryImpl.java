package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Voucher;
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

@Repository
public class CustomVoucherRepositoryImpl implements CustomVoucherRepository{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Voucher> getAllVouchers(String voucherId, LocalDate fromDate, LocalDate toDate) {
        Query query=new Query();
        if (fromDate != null && toDate != null) {
            query.addCriteria(Criteria.where("voucherDate").gte(fromDate).lte(toDate));
        }
        if (voucherId!=null)
            query.addCriteria(Criteria.where("voucherId").is(voucherId));
        return mongoTemplate.find(query, Voucher.class);
    }

    @Override
    public Page<Voucher> getAllVouchersWithPage(String voucherId, LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        Query query=new Query();
        if (fromDate != null && toDate != null) {
            query.addCriteria(Criteria.where("voucherDate").gte(fromDate).lte(toDate));
        }
        if (voucherId!=null)
            query.addCriteria(Criteria.where("voucherId").is(voucherId));

        long count = mongoTemplate.count(query, Voucher.class);
        query.with(pageable);
        List<Voucher> vouchers = mongoTemplate.find(query, Voucher.class);

        return new PageImpl<>(vouchers, pageable, count);
    }
}
