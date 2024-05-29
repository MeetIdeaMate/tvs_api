package com.techlambdas.delearmanagementapp.mapper;

import com.techlambdas.delearmanagementapp.model.Item;
import com.techlambdas.delearmanagementapp.request.ItemRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item mapItemRequestToItem(ItemRequest itemRequest);
    void updateItemFromRequest(ItemRequest itemRequest,  @MappingTarget Item existingItem);
}
