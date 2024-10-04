package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Category;
import com.techlambdas.delearmanagementapp.model.Item;
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
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
public class CustomItemRepositoryImpl implements CustomItemRepository{
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public List<Item> getAllItems(String itemId, String itemName, String partNo) {
        Query query=new Query();
        if (itemId!=null)
            query.addCriteria(Criteria.where("itemId").is(itemId));
        if (itemName != null)
            query.addCriteria(Criteria.where("itemName").regex(Pattern.compile(itemName, Pattern.CASE_INSENSITIVE)));
        if (partNo!=null)
            query.addCriteria(Criteria.where("partNo").regex(partNo));
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));

        return mongoTemplate.find(query, Item.class);
    }

    @Override
    public Page<Item> getAllItemsWithPage(String itemId, String itemName, String partNo, Pageable pageable, String hsnCode, String categoryName) {
        Query query=new Query();
        if (itemId!=null)
            query.addCriteria(Criteria.where("itemId").is(itemId));
        if (itemName != null)
            query.addCriteria(Criteria.where("itemName").regex(Pattern.compile(itemName, Pattern.CASE_INSENSITIVE)));
        if (partNo!=null)
            query.addCriteria(Criteria.where("partNo").regex(partNo));

        if(hsnCode != null)
            query.addCriteria(Criteria.where("hsnSacCode").regex(hsnCode));
        if (Optional.ofNullable(categoryName).isPresent()) {
            List<String> categoryIdList= getCategoryNameFromCategory(categoryName);
            query.addCriteria(Criteria.where("categoryId").in(categoryIdList));
        }



        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));
        long count = mongoTemplate.count(query, Item.class);
        query.with(pageable);
        List<Item> items = mongoTemplate.find(query, Item.class);

        return new PageImpl<>(items, pageable, count);
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
