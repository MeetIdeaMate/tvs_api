package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Vendor;
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
import java.util.regex.Pattern;

@Repository
public class CustomVendorRespositoryImpl implements CustomVendorRepository{

    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public List<Vendor> getAllVendors(String vendorId,String vendorName, String mobileNo, String city) {

        Query query=new Query();
        if (vendorId!=null)
            query.addCriteria(Criteria.where("vendorId").is(vendorId));
        if (vendorName != null)
            query.addCriteria(Criteria.where("vendorName").regex(Pattern.compile(vendorName, Pattern.CASE_INSENSITIVE)));
        if (mobileNo!=null)
            query.addCriteria(Criteria.where("mobileNo").regex(mobileNo));
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));

        if (city!=null)
            query.addCriteria(Criteria.where("city").regex(Pattern.compile(city,Pattern.CASE_INSENSITIVE)));
        return mongoTemplate.find(query, Vendor.class);
    }

    @Override
    public Page<Vendor> getAllVendorsWithPage(String vendorId, String vendorName, String mobileNo,String city, Pageable pageable) {
        Query query=new Query();
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));

        if (vendorId!=null)
            query.addCriteria(Criteria.where("vendorId").is(vendorId));
        if (vendorName != null)
            query.addCriteria(Criteria.where("vendorName").regex(Pattern.compile(vendorName, Pattern.CASE_INSENSITIVE)));
        if (mobileNo!=null)
            query.addCriteria(Criteria.where("mobileNo").regex(mobileNo));
        if (city!=null)
            query.addCriteria(Criteria.where("city").regex(Pattern.compile(city,Pattern.CASE_INSENSITIVE)));
        long count = mongoTemplate.count(query, Vendor.class);
        query.with(pageable);
        List<Vendor> vendors = mongoTemplate.find(query, Vendor.class);

        return new PageImpl<>(vendors, pageable, count);
    }

}
