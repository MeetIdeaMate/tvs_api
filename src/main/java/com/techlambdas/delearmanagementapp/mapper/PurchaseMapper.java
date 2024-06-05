package com.techlambdas.delearmanagementapp.mapper;

import com.techlambdas.delearmanagementapp.model.*;
import com.techlambdas.delearmanagementapp.request.PurchaseRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PurchaseMapper
{
    Purchase mapPurchaseRequestToPurchase(PurchaseRequest purchaseRequest);
    void updatePurchaseFromRequest(PurchaseRequest purchaseRequest,@MappingTarget Purchase existingPurchase);
}
