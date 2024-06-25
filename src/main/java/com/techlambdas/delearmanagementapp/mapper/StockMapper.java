package com.techlambdas.delearmanagementapp.mapper;

import com.techlambdas.delearmanagementapp.model.*;
import com.techlambdas.delearmanagementapp.request.StockRequest;
import com.techlambdas.delearmanagementapp.response.PurchaseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StockMapper {
    Stock mapStockRequestToStock(StockRequest stockRequest);

    void updateStockDetailFromRequest(StockRequest stockRequest,@MappingTarget Stock existingStock);

    PurchaseResponse toPurchaseItemResponse(ItemDetail itemDetail);

    Stock itemDetailToStock(ItemDetail itemDetail, String branchId);

    PurchaseItem itemDetailToPurchaseItem(ItemDetail itemDetail);

    SalesItem itemDetailToSalesItem(ItemDetail itemDetail);

    Stock toStock(ItemDetail itemDetail, String branchId);
}
