package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.model.Item;
import com.techlambdas.delearmanagementapp.request.ItemRequest;
import com.techlambdas.delearmanagementapp.response.ItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ItemService
{
    Item createItem(ItemRequest itemRequest);

    List<Item> getAllItems(String itemId, String itemName, String partNo);

    Item updateItem(String itemId, ItemRequest itemRequest);

    Page<ItemResponse> getAllItemsWithPage(String itemId, String itemName, String partNo, Pageable pageable, String categoryName, String hsnCode);

    Item getItemByIdCategoryIdPartNo(String categoryId, String partNo);

    Item findByPartNo(String partNo);

    Item updateAddons(Map<String,Double> addOns,String itemId);


}
