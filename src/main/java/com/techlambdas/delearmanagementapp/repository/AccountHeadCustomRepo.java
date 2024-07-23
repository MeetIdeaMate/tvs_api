package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.constant.PricingFormat;
import com.techlambdas.delearmanagementapp.model.AccountHead;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AccountHeadCustomRepo {
    Page<AccountHead> searchAccountHead(int page, int size, String accountHeadCode, String accountHeadName, PricingFormat pricingFormat, Boolean isCashierOps, boolean activeStatus, AccountType accountType, String transferFrom);

    List<AccountHead> getAccessAccountHeads();
}
