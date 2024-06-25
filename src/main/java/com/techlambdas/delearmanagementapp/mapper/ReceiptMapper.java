package com.techlambdas.delearmanagementapp.mapper;

import com.techlambdas.delearmanagementapp.model.Receipt;
import com.techlambdas.delearmanagementapp.request.ReceiptRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {
    Receipt mapReceiptRequestToReceipt(ReceiptRequest receiptRequest);

    void updateReceiptFromRequest(ReceiptRequest receiptRequest,@MappingTarget Receipt existingReceipt);
}
