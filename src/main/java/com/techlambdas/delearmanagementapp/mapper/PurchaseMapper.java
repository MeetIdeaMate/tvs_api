package com.techlambdas.delearmanagementapp.mapper;

import com.techlambdas.delearmanagementapp.model.Purchase;
import com.techlambdas.delearmanagementapp.request.PurchaseRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PurchaseMapper
{
    Purchase mapPurchaseRequestToPurchase(PurchaseRequest purchaseRequest);
    void updatePurchaseFromRequest(PurchaseRequest purchaseRequest,@MappingTarget Purchase existingPurchase);
}
