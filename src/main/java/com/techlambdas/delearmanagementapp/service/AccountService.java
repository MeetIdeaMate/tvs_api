package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.model.Account;
import com.techlambdas.delearmanagementapp.model.Statement;
import com.techlambdas.delearmanagementapp.request.AccountRequest;
import com.techlambdas.delearmanagementapp.response.AccountDataSummary;
import com.techlambdas.delearmanagementapp.response.Balance;
import com.techlambdas.delearmanagementapp.response.Ledger;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface AccountService {
        Account createAccountEntry(AccountRequest accountRequest);

        Account updateAccountEntry(String transacId,AccountRequest accountRequest);

        List<AccountDataSummary> getAccountSummaryByCollectionOperator(String operatorCode);

        List<AccountDataSummary> getAccountSummaryByAllCollectionOperator();

        Account createSettlementEntry(AccountRequest accountRequest,
                                      boolean recpay,String settlementId);

        Account generateAccountList(AccountRequest accountentry);



        List<Account> getAllAccountEntry();

        Page<Account> getByAccType(int page, int size, String financialYear, String accountHeadCode, String accountHeadName, String transactorId, AccountType transactType, String transactorName, String transactDesc, String shortNotes, String transactRefNo, String transactDetails, LocalDate transactDate,LocalDate fromDate, LocalDate toDate);

        Ledger getledger(LocalDate transacdate);

        Balance closingBalance(LocalDate transactDate);

        List<Balance> getFilAcc(LocalDate transactDate);

        Statement uploadFile(MultipartFile file);

        //   List<Account> getFilAcc( LocalDate tranasactDate);

}