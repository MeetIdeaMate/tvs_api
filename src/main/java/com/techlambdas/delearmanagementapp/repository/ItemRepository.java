package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface ItemRepository extends MongoRepository<Item,String> {
    Item findByItemId(String itemId);
    Optional<Item> findByCategoryIdAndPartNo(String category, String partNo);
    Item findByPartNo(String partNo);

    default String getItemName(String partNo)
    {
        Item item= findByPartNo(partNo);
        return (item !=null)?item.getItemName():"unknown item";
    }
    default String getCategoryId(String partNo)
    {
        Item item= findByPartNo(partNo);
        return (item !=null)?item.getCategoryId():"unknown item";
    }

    default String gethsnSacCode(String partNo)
    {
        Item item= findByPartNo(partNo);
        return (item !=null)?item.getHsnSacCode():"unknown PartNo";
    }
    default Map<String, Double> getAddOns(String partNo) {
        Item item = findByPartNo(partNo);
        return (item != null) ? item.getAddOns() : new HashMap<>();
    }
}
