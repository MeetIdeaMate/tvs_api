package com.techlambdas.delearmanagementapp.mapper;

import com.techlambdas.delearmanagementapp.model.Voucher;
import com.techlambdas.delearmanagementapp.request.VoucherRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VoucherMapper {
    Voucher mapVoucherRequestToVoucher(VoucherRequest voucherRequest);

    void updateVoucherFromRequest(VoucherRequest voucherRequest,@MappingTarget Voucher existingVoucher);
}
