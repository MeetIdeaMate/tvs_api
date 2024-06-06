package com.techlambdas.delearmanagementapp.mapper;


import com.techlambdas.delearmanagementapp.model.Sales;
import com.techlambdas.delearmanagementapp.request.SalesRequest;
import com.techlambdas.delearmanagementapp.request.SalesUpdateReq;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SalesMapper {

    Sales mapSalesRequestToSales(SalesRequest salesRequest);

    Sales mapSalesUpdateReqtoSales(SalesUpdateReq salesUpdateReq);
}
