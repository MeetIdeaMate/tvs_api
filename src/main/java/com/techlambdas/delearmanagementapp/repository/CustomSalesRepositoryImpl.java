package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.*;
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
public class CustomSalesRepositoryImpl implements  CustomSalesRepository{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Sales> getAllSales(String invoiceNo) {
        Query query=new Query();
    if (Optional.ofNullable(invoiceNo).isPresent()) {
        query.addCriteria(Criteria.where("invoiceNo").is(invoiceNo));
    }
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));

        return mongoTemplate.find(query, Sales.class);
    }

    @Override
    public Page<Sales> getAllSalesWithPage(String invoiceNo, String categoryName, LocalDate fromDate , LocalDate toDate ,Pageable pageable ) {
        Query query=new Query();
        if (Optional.ofNullable(invoiceNo).isPresent()) {
            query.addCriteria(Criteria.where("invoiceNo").is(invoiceNo));
        }
        if (Optional.ofNullable(categoryName).isPresent()) {
            List<String> categoryFromCatName = getCategoryNameFromCategory(categoryName);
            query.addCriteria(Criteria.where("itemDetails.categoryId").in(categoryFromCatName));
        }

        if (fromDate != null && toDate != null) {
            query.addCriteria(Criteria.where("invoiceDate").gte(fromDate).lte(toDate));
        }

        else if (fromDate != null) {
            query.addCriteria(Criteria.where("invoiceDate").gte(fromDate));
        } else if (toDate != null) {
            query.addCriteria(Criteria.where("invoiceDate").lte(toDate));
        }
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));

        long count = mongoTemplate.count(query, Sales.class);
        query.with(pageable);
        List<Sales> sales = mongoTemplate.find(query, Sales.class);

        return new PageImpl<>(sales, pageable, count);
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

}
