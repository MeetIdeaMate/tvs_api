package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<Item,String> {
    Item findByItemId(String itemId);
}
