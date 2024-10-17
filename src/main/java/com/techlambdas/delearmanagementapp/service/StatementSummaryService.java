package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.model.BankStatementSummary;
import com.techlambdas.delearmanagementapp.request.StatementSummaryUpdateReq;

import java.time.LocalDate;

public interface StatementSummaryService {



    void createStatementSummary(String statementId);


    BankStatementSummary getStatementSummary(String statementId, AccountType accountType, String accountHead, LocalDate fromDate, LocalDate toDate);

    BankStatementSummary getStatementSummaryById(String statementId);

    BankStatementSummary updateStatementSummary(String statementId, StatementSummaryUpdateReq statementSummaryUpdateReq);
}
