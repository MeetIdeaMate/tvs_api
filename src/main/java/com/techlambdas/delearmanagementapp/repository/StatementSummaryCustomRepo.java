package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.model.BankStatementSummary;

public interface StatementSummaryCustomRepo {

    BankStatementSummary getStatementSummary(AccountType accountType,String accountHeadName,String statementId);

}
