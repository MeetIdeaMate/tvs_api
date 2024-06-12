package com.techlambdas.delearmanagementapp.repository;

import com.mongodb.BasicDBObject;
import com.techlambdas.delearmanagementapp.constant.Status;
import com.techlambdas.delearmanagementapp.constant.StockStatus;
import com.techlambdas.delearmanagementapp.model.Category;
import com.techlambdas.delearmanagementapp.model.Item;
import com.techlambdas.delearmanagementapp.model.Stock;

import com.techlambdas.delearmanagementapp.response.TransferResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CustomStockRepositoryImpl implements CustomStockRepository{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Stock> getAllStocks(String partNo,String itemName,String engineNo,String frameNo,String categoryName) {
        Query query=new Query();
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
        if (engineNo!=null)
            query.addCriteria(Criteria.where("mainSpecValue.engineNo").is(engineNo));
        if (frameNo!=null)
            query.addCriteria(Criteria.where("mainSpecValue.frameNo").is(frameNo));
            query.addCriteria(Criteria.where("stockStatus").is(StockStatus.Available));
        return mongoTemplate.find(query, Stock.class);
    }

    @Override
    public Page<Stock> getAllStocksWithPage(String partNo,String itemName, String engineNo,String frameNo,Pageable pageable,String categoryName) {
        Query query=new Query();
        query.addCriteria(Criteria.where("stockStatus").is(StockStatus.Available));
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
        if (transferFromBranch!=null)
            query.addCriteria(Criteria.where("branchId").is(transferFromBranch));
        return mongoTemplate.find(query, Stock.class);
    }

   /* @Override
    public List<Stock> findTransferDetailsByBranchId(String branchId) {
        Query query = new Query();
        if (branchId != null) {
            query.addCriteria(Criteria.where("transferDetails.transferFromBranch").is(branchId));

        }
        query.addCriteria(Criteria.where("transferDetails.status").is(Status.CURRENT));
        List<Stock> stocks = mongoTemplate.find(query, Stock.class);
       return stocks;
           *//* MatchOperation matchBranch = Aggregation.match(Criteria.where("transferDetails.transferFromBranch").is(branchId));
            UnwindOperation unwindTransferDetails = Aggregation.unwind("transferDetails");
            MatchOperation matchCurrentStatus = Aggregation.match(Criteria.where("transferDetails.status").is(Status.CURRENT));

            Aggregation aggregation = Aggregation.newAggregation(
                    matchBranch,
                    unwindTransferDetails,
                    matchCurrentStatus,
                    Aggregation.replaceRoot("transferDetails")
            );

            AggregationResults<TransferDetail> results = mongoTemplate.aggregate(aggregation, "stock", TransferDetail.class);
            return results.getMappedResults();*//*

    }
*/
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
    public List<TransferResponse> findTransferDetails(String branchId) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("transferDetails"),
                Aggregation.match(Criteria.where("transferDetails.status").is(Status.CURRENT)),
                Aggregation.lookup("branches", "transferDetails.transferFromBranch", "branchId", "fromBranch"),
                Aggregation.unwind("fromBranch"),
                Aggregation.lookup("branches", "transferDetails.transferToBranch", "branchId", "toBranch"),
                Aggregation.unwind("toBranch"),
                Aggregation.group("transferDetails.transferId")
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
                        .first("transferDetails.receivedDate").as("receivedDate"),

                Aggregation.project()
                        .and("transferId").as("transferId")
                        .and("fromBranchId").as("fromBranchId")
                        .and("fromBranchName").as("fromBranchName")
                        .and("toBranchId").as("toBranchId")
                        .and("toBranchName").as("toBranchName")
                        .and("totalQuantity").as("totalQuantity")
                        .and("transferDate").as("transferDate")
                        .and("receivedDate").as("receivedDate")
                        .and("transferItems").as("transferItems")
        );
        AggregationResults<TransferResponse> results = mongoTemplate.aggregate(aggregation, "stock", TransferResponse.class);
        return results.getMappedResults();
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
