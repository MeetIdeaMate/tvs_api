package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.ItemMapper;
import com.techlambdas.delearmanagementapp.model.Category;
import com.techlambdas.delearmanagementapp.model.Item;
import com.techlambdas.delearmanagementapp.model.Vendor;
import com.techlambdas.delearmanagementapp.repository.CustomItemRepository;
import com.techlambdas.delearmanagementapp.repository.ItemRepository;
import com.techlambdas.delearmanagementapp.request.ItemRequest;
import com.techlambdas.delearmanagementapp.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CustomItemRepository customItemRepository;
    @Override
    public Item createItem(ItemRequest itemRequest) {
        try {
            Item item = itemMapper.mapItemRequestToItem(itemRequest);
            item.setItemId(RandomIdGenerator.getRandomId());
            return itemRepository.save(item);
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public List<Item> getAllItems(String itemId, String itemName, String partNo) {
        List<Item> items=customItemRepository.getAllItems(itemId,itemName,partNo);
        return items;
    }

    @Override
    public Item updateItem(String itemId, ItemRequest itemRequest) {
        try {
            Item existingItem = itemRepository.findByItemId(itemId);
            if (existingItem == null)
                throw new DataNotFoundException("item not found with ID: " + itemId);
            itemMapper.updateItemFromRequest(itemRequest, existingItem);
            return itemRepository.save(existingItem);
        }catch (DataNotFoundException ex) {
            throw new DataNotFoundException("Data not found --" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public Page<Item> getAllItemsWithPage(String itemId, String itemName, String partNo, Pageable pageable) {
        return customItemRepository.getAllItemsWithPage(itemId,itemName,partNo, pageable);
    }
}
