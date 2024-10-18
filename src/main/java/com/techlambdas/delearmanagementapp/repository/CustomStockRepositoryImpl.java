package com.techlambdas.delearmanagementapp.repository;

import com.mongodb.BasicDBObject;
import com.techlambdas.delearmanagementapp.constant.Status;
import com.techlambdas.delearmanagementapp.constant.StockStatus;
import com.techlambdas.delearmanagementapp.constant.TransferType;
import com.techlambdas.delearmanagementapp.model.Category;
import com.techlambdas.delearmanagementapp.model.Item;
import com.techlambdas.delearmanagementapp.model.Stock;

import com.techlambdas.delearmanagementapp.response.StockDTO;
import com.techlambdas.delearmanagementapp.response.TransferResponse;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CustomStockRepositoryImpl implements CustomStockRepository{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Stock> getAllStocks(String partNo,String itemName,String keyValue,String categoryName,String branchId) {
        Query query=new Query();
        query.addCriteria(Criteria.where("quantity").gt(0));
        if (partNo!=null)
            query.addCriteria(Criteria.where("partNo").is(partNo));
        if (itemName!=null) {
            List<String> itemPartNo = getItemNameFromItems(itemName);
            query.addCriteria(Criteria.where("partNo").in(itemPartNo));
        }
        if (Optional.ofNullable(categoryName).isPresent()) {
            List<String> categoryIdList= getCategoryNameFromCategory(categoryName);
            query.addCriteria(Criteria.where("categoryId").in(categoryIdList));
        }
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));

        if (branchId!=null)
            query.addCriteria(Criteria.where("branchId").is(branchId));
        if (keyValue!=null)
            query.addCriteria(Criteria.where("itemDetails.mainSpecValue").elemMatch(Criteria.where("$eq").is(keyValue)));
        query.addCriteria(Criteria.where("stockStatus").is(StockStatus.Available));
        return mongoTemplate.find(query, Stock.class);
    }
    @Override
    public Page<Stock> getAllStocksWithPage(String partNo,String itemName, String keyValue,Pageable pageable,String categoryName,String branchId) {
        Query query=new Query();
        query.addCriteria(Criteria.where("quantity").gt(0));
        query.addCriteria(Criteria.where("stockStatus").is(StockStatus.Available));
        if (branchId!=null)
            query.addCriteria(Criteria.where("branchId").is(branchId));
        if (partNo!=null)
            query.addCriteria(Criteria.where("partNo").is(partNo));
        if (itemName!=null)
        {
            List<String> itemPartNo = getItemNameFromItems(itemName);
            query.addCriteria(Criteria.where("partNo").in(itemPartNo));
        }
        if (Optional.ofNullable(categoryName).isPresent()) {
            List<String> categoryIdList= getCategoryNameFromCategory(categoryName);
            query.addCriteria(Criteria.where("categoryId").in(categoryIdList));
        }
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));
        if (keyValue!=null)
            query.addCriteria(Criteria.where("itemDetails.mainSpecValue").elemMatch(Criteria.where("$eq").is(keyValue)));
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

    @Override
    public List<TransferResponse> findTransferDetails(String fromBranchId, String toBranchId, TransferType transferType) {

        List<AggregationOperation> stages = new ArrayList<>();
        stages.add(Aggregation.unwind("transferDetails"));
        stages.add(Aggregation.match(Criteria.where("transferDetails.status").is(Status.CURRENT)));

        if (Optional.ofNullable(fromBranchId).isPresent()) {
            stages.add(Aggregation.match(Criteria.where("transferDetails.transferFromBranch").is(fromBranchId)));
        }
        if (Optional.ofNullable(toBranchId).isPresent()) {
            stages.add(Aggregation.match(Criteria.where("transferDetails.transferToBranch").is(toBranchId)));
        }
        stages.add(Aggregation.lookup("branches", "transferDetails.transferFromBranch", "branchId", "fromBranch"));
        stages.add(Aggregation.unwind("fromBranch"));
        stages.add(Aggregation.lookup("branches", "transferDetails.transferToBranch", "branchId", "toBranch"));
        stages.add(Aggregation.unwind("toBranch"));
        stages.add(Aggregation.group("transferDetails.transferId")
                .first("transferDetails.transferId").as("transferId")
                .first("transferDetails.transferFromBranch").as("fromBranchId")
                .first("fromBranch.branchName").as("fromBranchName")
                .first("transferDetails.transferToBranch").as("toBranchId")
                .first("toBranch.branchName").as("toBranchName")
                .sum("quantity").as("totalQuantity")
                .push(new BasicDBObject("partNo", "$partNo")
                        .append("categoryId", "$categoryId")
                        .append("mainSpecValue", "$mainSpecValue")
                        .append("quantity", "$quantity"))
                .as("transferItems")
                .first("transferDetails.transferDate").as("transferDate")
                .first("transferDetails.receivedDate").as("receivedDate"));
        stages.add(Aggregation.sort(Sort.Direction.DESC, "transferDate"));
        stages.add(Aggregation.project()
                .and("transferId").as("transferId")
                .and("fromBranchId").as("fromBranchId")
                .and("fromBranchName").as("fromBranchName")
                .and("toBranchId").as("toBranchId")
                .and("toBranchName").as("toBranchName")
                .and("totalQuantity").as("totalQuantity")
                .and("transferDate").as("transferDate")
                .and("receivedDate").as("receivedDate")
                .and("transferItems").as("transferItems"));

        Aggregation aggregation = Aggregation.newAggregation(stages);

        AggregationResults<TransferResponse> results = mongoTemplate.aggregate(aggregation, "stock", TransferResponse.class);

        return results.getMappedResults();
    }


    @Override
    public List<Stock> findStocksByTransferId(String transferId) {
        Query query=new Query();
        query.addCriteria(Criteria.where("transferDetails.transferId").is(transferId));
        query.addCriteria(Criteria.where("transferDetails.status").is(Status.CURRENT));
        query.addCriteria(Criteria.where("stockStatus").is(StockStatus.Transfer));
        return mongoTemplate.find(query, Stock.class);
    }
    @Override
    public Page<StockDTO> getCumulativeStockWithPage(String partNo, String itemName, String keyValue, Pageable pageable, String categoryName, String branchId) {
        Criteria criteria = new Criteria();
        if (partNo != null && !partNo.isEmpty()) {
            criteria.and("partNo").is(partNo);
        }else if (itemName!=null&&!itemName.isEmpty())
        {
            List<String> itemPartNo = getItemNameFromItems(itemName);
            criteria.and("partNo").in(itemPartNo);
        }
        if (keyValue != null && !keyValue.isEmpty()) {
            criteria.orOperator(
                    Criteria.where("mainSpecValue").regex(keyValue, "i"),
                    Criteria.where("specificationsValue").regex(keyValue, "i")
            );
        }
        if (categoryName != null && !categoryName.isEmpty()) {
            List<String> categoryIdList = getCategoryNameFromCategory(categoryName);
            criteria.and("categoryId").in(categoryIdList);
        }
        if (branchId != null && !branchId.isEmpty()) {
            criteria.and("branchId").is(branchId);
        }
        criteria.and("quantity").gt(0);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.lookup("items", "partNo", "partNo", "item"),
                Aggregation.unwind("item", true),
                Aggregation.lookup("branches", "branchId", "branchId", "branch"),
                Aggregation.unwind("branch", true),
                Aggregation.lookup("category", "categoryId", "categoryId", "category"),
                Aggregation.unwind("category", true),
                Aggregation.group("partNo", "branchId","stockStatus")
                        .sum("quantity").as("totalQuantity")
                        .first("categoryId").as("categoryId")
                        .first("stockStatus").as("stockStatus")
                        .first("hsnSacCode").as("hsnSacCode")
                        .first("item.itemName").as("itemName")
                        .first("branch.branchName").as("branchName")
                        .first("category.categoryName").as("categoryName")
                        .push(new BasicDBObject("stockId", "$stockId")
                                .append("quantity", "$quantity")
                                .append("mainSpecValue", "$mainSpecValue")
                        ).as("stockItems"),
                Aggregation.project("totalQuantity", "partNo", "branchId", "categoryId", "stockStatus", "stockItems","itemName","branchName","categoryName"),
                Aggregation.skip(pageable.getOffset()),
                Aggregation.limit(pageable.getPageSize())
        );
        AggregationResults<StockDTO> results = mongoTemplate.aggregate(aggregation, "stock", StockDTO.class);
        List<StockDTO> stockDTOList = results.getMappedResults();
        Aggregation countAggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("partNo", "branchId")
        );
        AggregationResults<Document> countResults = mongoTemplate.aggregate(countAggregation, "stock", Document.class);
        long totalCount = countResults.getMappedResults().size();

        return new PageImpl<>(stockDTOList, pageable, totalCount);
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
