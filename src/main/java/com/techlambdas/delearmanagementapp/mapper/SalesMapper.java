package com.techlambdas.delearmanagementapp.mapper;


import com.techlambdas.delearmanagementapp.model.Purchase;
import com.techlambdas.delearmanagementapp.model.Sales;
import com.techlambdas.delearmanagementapp.request.SalesRequest;
import com.techlambdas.delearmanagementapp.request.SalesUpdateReq;
import com.techlambdas.delearmanagementapp.utils.RandomIdGenerator;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SalesMapper {
    @Mapping(target = "salesId", ignore = true)
    Sales mapSalesRequestToSales(SalesRequest salesRequest);

    Sales mapSalesUpdateReqtoSales(SalesUpdateReq salesUpdateReq);
    @AfterMapping
    default void setSalesId(@MappingTarget Sales sales) {
        sales.setSalesId(RandomIdGenerator.getRandomId());
    }

}
