package com.techlambdas.delearmanagementapp.repository;


import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.model.Account;
import com.techlambdas.delearmanagementapp.response.AccountDataSummary;
import com.techlambdas.delearmanagementapp.response.Balance;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface AccountCustomRepo {

    List<AccountDataSummary> getAggregateResultByAllOperator();


    List<AccountDataSummary> getAggregateResultByOperator(String operatorCode);

    Page<Account> getByAccType(int page, int size, String financialYear, String accountHeadCode, String accountHeadName, String transactorId, AccountType transactType, String transactorName, String transactDesc, String shortNotes, String transactRefNo, String transactDetails, LocalDate transactDate, LocalDate fromDate, LocalDate toDate);



    List<Balance> filterAcc(LocalDate transactDate);

    Balance closingBalance(LocalDate transactDate);


    List<Account> findByTransactDateBetween(LocalDate transacDate, LocalDate endDate);
}
