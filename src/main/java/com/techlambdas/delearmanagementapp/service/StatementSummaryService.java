package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.model.BankStatementSummary;

public interface StatementSummaryService {



    void createStatementSummary(String statementId);


    BankStatementSummary getStatementSummary(String statementId);
}
