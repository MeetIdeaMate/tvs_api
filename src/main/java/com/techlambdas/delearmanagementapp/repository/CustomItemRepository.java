package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomItemRepository {
    List<Item> getAllItems(String itemId, String itemName, String partNo);

    Page<Item> getAllItemsWithPage(String itemId, String itemName, String partNo, Pageable pageable,  String hsnCode, String categoryName);
}
