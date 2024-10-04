package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.CommonMapper;
import com.techlambdas.delearmanagementapp.mapper.ItemMapper;
import com.techlambdas.delearmanagementapp.model.Item;
import com.techlambdas.delearmanagementapp.repository.CustomItemRepository;
import com.techlambdas.delearmanagementapp.repository.ItemRepository;
import com.techlambdas.delearmanagementapp.request.ItemRequest;
import com.techlambdas.delearmanagementapp.response.ItemResponse;
import com.techlambdas.delearmanagementapp.response.ReceiptResponse;
import com.techlambdas.delearmanagementapp.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService{
    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private CommonMapper commonMapper;
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
    public Page<ItemResponse> getAllItemsWithPage(String itemId, String itemName, String partNo, Pageable pageable, String categoryName, String hsnCode) {
        Page<Item> items = customItemRepository.getAllItemsWithPage(itemId,itemName,partNo, pageable ,hsnCode ,categoryName);

        List<ItemResponse> receiptResponses = items.stream()
                .map(commonMapper::mapItemToItemResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(receiptResponses,pageable,items.getTotalElements());
    }
    @Override
    public Item getItemByIdCategoryIdPartNo( String categoryId, String partNo) {
        Optional<Item> itemOptional = itemRepository.findByCategoryIdAndPartNo(categoryId, partNo);
        return itemOptional.orElseThrow(() -> new RuntimeException("Item not found"));
    }

    @Override
    public Item findByPartNo(String partNo) {
        return itemRepository.findByPartNo(partNo);
    }

    @Override
    public Item updateAddons(Map<String, Double> addOns, String itemId) {
      try{
          Item existingItem = itemRepository.findByItemId(itemId);
          if (existingItem == null)
              throw new DataNotFoundException("item not found with ID: " + itemId);
          existingItem.setAddOns(addOns);
          return itemRepository.save(existingItem);
      }catch (DataNotFoundException ex) {
          throw new DataNotFoundException("Data not found --" + ex.getMessage());
      } catch (Exception ex) {
          throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
      }
    }
}
