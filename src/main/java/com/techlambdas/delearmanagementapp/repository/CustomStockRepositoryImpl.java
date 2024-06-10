package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Item;
import com.techlambdas.delearmanagementapp.model.Stock;
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
import java.util.stream.Collectors;

@Repository
public class CustomStockRepositoryImpl implements CustomStockRepository{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Stock> getAllStocks(String partNo,String itemName,String engineNo,String frameNo) {
        Query query=new Query();
        if (partNo!=null)
            query.addCriteria(Criteria.where("partNo").is(partNo));
        if (itemName!=null) {
            List<String> itemPartNo = getItemNameFromItems(itemName);
            query.addCriteria(Criteria.where("partNo").in(itemPartNo));
        }
        if (engineNo!=null)
            query.addCriteria(Criteria.where("mainSpecValue.engineNo").is(engineNo));
        if (frameNo!=null)
            query.addCriteria(Criteria.where("mainSpecValue.frameNo").is(frameNo));
        return mongoTemplate.find(query, Stock.class);
    }

    @Override
    public Page<Stock> getAllStocksWithPage(String partNo,String itemName, String engineNo,String frameNo,Pageable pageable) {
        Query query=new Query();
        if (partNo!=null)
            query.addCriteria(Criteria.where("partNo").is(partNo));
        if (itemName!=null)
        {
            List<String> itemPartNo = getItemNameFromItems(itemName);
            query.addCriteria(Criteria.where("partNo").in(itemPartNo));
        }
        if (engineNo!=null)
            query.addCriteria(Criteria.where("mainSpecValue.engineNo").is(engineNo));
        if (frameNo!=null)
            query.addCriteria(Criteria.where("mainSpecValue.frameNo").is(frameNo));
        long count=mongoTemplate.count(query,Stock.class);
        query.with(pageable);
        List<Stock> stocks=mongoTemplate.find(query,Stock.class);
        return new PageImpl<>(stocks,pageable,count);
    }

    @Override
    public List<Stock> findByPartNoAndBranchId(String partNo, String transferFromBranch) {
        Query query=new Query();
        if (partNo!=null)
            query.addCriteria(Criteria.where("partNo").is(partNo));
        if (partNo!=null) {
            query.addCriteria(Criteria.where("partNo").in(partNo));
        }
        if (transferFromBranch!=null)
            query.addCriteria(Criteria.where("branchId").is(transferFromBranch));
        return mongoTemplate.find(query, Stock.class);
    }

    public List<String>getItemNameFromItems(String itemName)
    {
        Query query=new Query();
        if (itemName!=null){
            query.addCriteria(Criteria.where("itemName").regex("^.*"+itemName+".*","i"));
        }
        List<Item> itemList=mongoTemplate.find(query, Item.class);
        return itemList.stream().map(Item::getPartNo).collect(Collectors.toList());
    }
}
