package com.techlambdas.delearmanagementapp.mapper;

import com.techlambdas.delearmanagementapp.model.Account;
import com.techlambdas.delearmanagementapp.request.AccountRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
@Mapper(componentModel = "Spring")
public interface AccountMapper {
    Account mapAccountRequestToAccount(AccountRequest accountRequest);

    void updateExistingAccountHead( AccountRequest accountRequest,@MappingTarget Account account);

}