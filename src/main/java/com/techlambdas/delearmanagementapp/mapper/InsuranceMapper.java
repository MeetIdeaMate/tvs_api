package com.techlambdas.delearmanagementapp.mapper;


import com.techlambdas.delearmanagementapp.model.Insurance;
import com.techlambdas.delearmanagementapp.request.InsuranceRequest;
import com.techlambdas.delearmanagementapp.utils.RandomIdGenerator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
@Mapper(componentModel = "spring")
public interface InsuranceMapper {

    Insurance mapReqToEntity(InsuranceRequest insuranceRequest);
    Insurance updateReqToEntity(InsuranceRequest insuranceRequest, @MappingTarget Insurance insurance);
}
