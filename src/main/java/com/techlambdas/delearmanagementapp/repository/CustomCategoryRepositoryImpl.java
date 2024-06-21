package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Category;
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
public class CustomCategoryRepositoryImpl implements CustomCategoryRepository{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Category> getAllCategorys(String categoryId, String categoryName) {
        Query query=new Query();
        if (categoryId!=null)
            query.addCriteria(Criteria.where("categoryId").is(categoryId));
        if (categoryName != null)
            query.addCriteria(Criteria.where("categoryName").regex(Pattern.compile(categoryName, Pattern.CASE_INSENSITIVE)));
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));

        return mongoTemplate.find(query, Category.class);
    }

    @Override
    public Page<Category> getAllCaregorysWithPage(String categoryId, String categoryName, Pageable pageable) {
        Query query=new Query();
        if (categoryId!=null)
            query.addCriteria(Criteria.where("categoryId").is(categoryId));
        if (categoryName != null)
            query.addCriteria(Criteria.where("categoryName").regex(Pattern.compile(categoryName, Pattern.CASE_INSENSITIVE)));
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));
        long count = mongoTemplate.count(query, Category.class);
        query.with(pageable);
        List<Category> categorys = mongoTemplate.find(query, Category.class);
        return new PageImpl<>(categorys, pageable, count);
    }
}
