package com.techlambdas.delearmanagementapp.mapper;

import com.techlambdas.delearmanagementapp.model.Vendor;
import com.techlambdas.delearmanagementapp.request.VendorRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VendorMapper
{
    Vendor mapVendorRequestToVendor(VendorRequest vendorRequest);
    void updateVendorFromRequest(VendorRequest request, @MappingTarget Vendor vendor);
}
