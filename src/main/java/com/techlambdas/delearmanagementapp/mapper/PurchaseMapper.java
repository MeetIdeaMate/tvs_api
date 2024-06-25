package com.techlambdas.delearmanagementapp.mapper;

import com.techlambdas.delearmanagementapp.model.*;
import com.techlambdas.delearmanagementapp.request.ItemDetailRequest;
import com.techlambdas.delearmanagementapp.request.PurchaseRequest;
import com.techlambdas.delearmanagementapp.response.ItemDetailResponse;
import com.techlambdas.delearmanagementapp.response.PurchaseResponse;
import com.techlambdas.delearmanagementapp.utils.RandomIdGenerator;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {
    @Mapping(target = "purchaseNo", ignore = true)
    @Mapping(target = "purchaseId", ignore = true)
    @Mapping(target = "itemDetails", ignore = true)
    Purchase mapPurchaseRequestToPurchase(PurchaseRequest purchaseRequest);

    @AfterMapping
    default void setPurchaseId(@MappingTarget Purchase purchase) {
        purchase.setPurchaseId(RandomIdGenerator.getRandomId());
    }
    @Mapping(target = "quantity", ignore = true)
    @Mapping(target =  "mainSpecValue",ignore = true)
    ItemDetail mapItemDetailRequestToItemDetail(ItemDetailRequest request);

    default List<ItemDetail> mapItemDetailRequestToItemDetailsMainSpecPresent(ItemDetailRequest request) {
        List<ItemDetail> itemDetails = new ArrayList<>();
        for (  int i=0; i< request.getQuantity(); i++) {
            ItemDetail itemDetail = mapItemDetailRequestToItemDetail(request);
            itemDetail.setQuantity(1);
            itemDetail.setValue(request.getUnitRate());
            itemDetail.setMainSpecValue(request.getMainSpecValues().get(i));
            itemDetails.add(itemDetail);
        }

        return itemDetails;
    }
//    void updatePurchaseFromRequest(PurchaseRequest purchaseRequest, @MappingTarget Purchase existingPurchase);

    PurchaseResponse mapEntityWithResponse(Purchase purchase);

    ItemDetailResponse mapItemDetailResponseWithItemDetail(ItemDetail itemDetail);

    ItemDetail mapItemDetailRequestToItemDetailsWithoutMainSpec(ItemDetailRequest itemDetailRequest);
}
