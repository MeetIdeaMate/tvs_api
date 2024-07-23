package com.techlambdas.delearmanagementapp.service;


import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.constant.PricingFormat;
import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.exception.InvalidDataException;
import com.techlambdas.delearmanagementapp.model.Account;
import com.techlambdas.delearmanagementapp.model.AccountHead;
import com.techlambdas.delearmanagementapp.repository.AccountHeadRepository;
import com.techlambdas.delearmanagementapp.repository.AccountRepository;
import com.techlambdas.delearmanagementapp.request.AccountRequest;
import com.techlambdas.delearmanagementapp.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService{


    @Autowired
    private AccountRepository accountRepository;


    @Override
    public Account createAccountEntry(AccountRequest accountRequest) {
        return null;
    }

    @Override
    public Account updateAccountEntry(String transacId, AccountRequest accountRequest) {
        return null;
    }

    @Override
    public List<Account> getAllAccountEntry() {
        return null;
    }

    @Override
    public Page<Account> getByAccType(int page, int size, String financialYear, String accountHeadCode, String accountHeadName, String transactorId, AccountType transactType, String transactorName, String transactDesc, String shortNotes, String transactRefNo, String transactDetails, LocalDate transactDate, LocalDate fromDate, LocalDate toDate) {
        return null;
    }
}
