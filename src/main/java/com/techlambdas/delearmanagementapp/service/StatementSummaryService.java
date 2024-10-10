package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.model.BankStatementSummary;
import com.techlambdas.delearmanagementapp.request.StatementSummaryUpdateReq;

public interface StatementSummaryService {



    void createStatementSummary(String statementId);


    BankStatementSummary getStatementSummary(String statementId, AccountType accountType,String accountHead);

    BankStatementSummary getStatementSummaryById(String statementId);

    void updateStatementSummary(String statementId, StatementSummaryUpdateReq statementSummaryUpdateReq);
}
