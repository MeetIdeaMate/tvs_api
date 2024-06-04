package com.techlambdas.delearmanagementapp.mapper;

import com.techlambdas.delearmanagementapp.model.*;
import com.techlambdas.delearmanagementapp.repository.BranchRepository;
import com.techlambdas.delearmanagementapp.request.PurchaseRequest;
import com.techlambdas.delearmanagementapp.response.*;
import com.techlambdas.delearmanagementapp.service.BranchService;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface PurchaseMapper
{
    ItemDetailsWithPartNoResponse toItemDetailsWithPartNoResponse(ItemDetail itemDetails);
    Purchase mapPurchaseRequestToPurchase(PurchaseRequest purchaseRequest);
    void updatePurchaseFromRequest(PurchaseRequest purchaseRequest,@MappingTarget Purchase existingPurchase);
}
