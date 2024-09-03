package com.techlambdas.delearmanagementapp.mapper;

import com.techlambdas.delearmanagementapp.model.AccountHead;
import com.techlambdas.delearmanagementapp.request.AccountHeadRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "Spring")
public interface AccountHeadMapper {
    AccountHead mapAccountHeadRequestToAccountHead(AccountHeadRequest accountHeadRequest);

    void updateExistingAccountHead( AccountHeadRequest ahreq,@MappingTarget AccountHead accountHead);
}