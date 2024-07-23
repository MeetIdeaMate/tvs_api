package com.techlambdas.delearmanagementapp.mapper;

import com.techlambdas.delearmanagementapp.model.AccessControl;
import com.techlambdas.delearmanagementapp.request.AccessControlRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccessControlMapper {


    AccessControl toEntity(AccessControlRequest request);
    void updateAccessControlFromRequest(AccessControlRequest request, @MappingTarget AccessControl accessControl);

}
