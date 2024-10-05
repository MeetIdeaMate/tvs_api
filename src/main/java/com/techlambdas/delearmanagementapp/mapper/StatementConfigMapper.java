package com.techlambdas.delearmanagementapp.mapper;

import com.techlambdas.delearmanagementapp.model.StatementConfig;
import com.techlambdas.delearmanagementapp.request.StatementConfigReq;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StatementConfigMapper {
    StatementConfig mapReqToEntity(StatementConfigReq statementConfigReq);

    StatementConfig updateReqToEntity(StatementConfigReq statementConfigReq , @MappingTarget StatementConfig statementConfig);
}
