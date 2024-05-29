package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Transport;
import com.techlambdas.delearmanagementapp.model.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.regex.Pattern;

@Repository
public class CustomTransportRepositoryImpl implements CustomTransportRepository{
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public List<Transport> getAllTransports(String transportId, String transportName, String mobileNo, String city) {
        Query query=new Query();
        if (transportId!=null)
            query.addCriteria(Criteria.where("transportId").is(transportId));
        if (transportName != null)
            query.addCriteria(Criteria.where("transportName").regex(Pattern.compile(transportName, Pattern.CASE_INSENSITIVE)));
        if (mobileNo!=null)
            query.addCriteria(Criteria.where("mobileNo").regex(mobileNo));
        if (city!=null)
            query.addCriteria(Criteria.where("city").regex(Pattern.compile(city,Pattern.CASE_INSENSITIVE)));
        return mongoTemplate.find(query, Transport.class);
    }

    @Override
    public Page<Transport> getAllTransportsWithPage(String transportId, String transportName, String mobileNo, String city, Pageable pageable) {
        Query query=new Query();
        if (transportId!=null)
            query.addCriteria(Criteria.where("transportId").is(transportId));
        if (transportName != null)
            query.addCriteria(Criteria.where("transportName").regex(Pattern.compile(transportName, Pattern.CASE_INSENSITIVE)));
        if (mobileNo!=null)
            query.addCriteria(Criteria.where("mobileNo").regex(mobileNo));
        if (city!=null)
            query.addCriteria(Criteria.where("city").regex(Pattern.compile(city,Pattern.CASE_INSENSITIVE)));
        long count = mongoTemplate.count(query, Transport.class);
        query.with(pageable);
        List<Transport> transports = mongoTemplate.find(query, Transport.class);

        return new PageImpl<>(transports, pageable, count);
    }
}
