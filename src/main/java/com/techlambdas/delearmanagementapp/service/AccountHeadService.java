package com.techlambdas.delearmanagementapp.service;
import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.constant.PricingFormat;
import com.techlambdas.delearmanagementapp.model.AccountHead;
import com.techlambdas.delearmanagementapp.request.AccountHeadRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AccountHeadService {
    AccountHead createAccountHead(AccountHeadRequest accountHeadRequest);



    Page<AccountHead> searchAccountHead(int page, int size, String accountHeadCode, String accountHeadName, PricingFormat pricingFormat, Boolean isCashierOps, boolean activeStatus, AccountType accountType, String transferFrom);

    AccountHead getAccountHead(String accountHeadCode);

    String checkAccountHeadCode(String accuntHeadCode);

    AccountHead updateAccountHead(String id, AccountHeadRequest accountHeadRequest);

    boolean deleteAccountHead(String id);


    List<AccountHead> getAllAccHead(AccountType accountType);
}
