package com.techlambdas.delearmanagementapp.mapper;

import com.techlambdas.delearmanagementapp.model.Stock;
import com.techlambdas.delearmanagementapp.request.StockRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StockMapper {
    Stock mapStockRequestToStock(StockRequest stockRequest);

    void updateStockDetailFromRequest(StockRequest stockRequest,@MappingTarget Stock existingStock);
}
