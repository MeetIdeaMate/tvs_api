package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.model.Account;
import com.techlambdas.delearmanagementapp.request.AccountRequest;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface AccountService {
    Account createAccountEntry(AccountRequest accountRequest);

    Account updateAccountEntry(String transacId, AccountRequest accountRequest);

    List<Account> getAllAccountEntry();

    Page<Account> getByAccType(int page, int size, String financialYear, String accountHeadCode, String accountHeadName, String transactorId, AccountType transactType, String transactorName, String transactDesc, String shortNotes, String transactRefNo, String transactDetails, LocalDate transactDate, LocalDate fromDate, LocalDate toDate);




}