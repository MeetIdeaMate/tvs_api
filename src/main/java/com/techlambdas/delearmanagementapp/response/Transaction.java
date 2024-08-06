package com.techlambdas.delearmanagementapp.response;

import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.model.Account;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class Transaction {
    private String id;
    private String transacId;
    private String financialYear;
    private LocalDate transactDate;
    private String accountHeadCode;
    private String accountHeadName;
    private String shortNotes;
    private String transactRefNo;
    private String transactDetails;
    private AccountType transactType;
    private String transactor;
    private String transactorId;
    private String transactorType;
    private double amount;


    public Transaction(Account account) {
        this.id = account.getId();
        this.transacId = account.getTransacId();
        this.financialYear = account.getFinancialYear();
        this.accountHeadCode = account.getAccountHeadCode();
        this.accountHeadName = account.getAccountHeadName();
        this.shortNotes = account.getShortNotes();
        this.transactRefNo = account.getTransactRefNo();
        this.transactDetails = account.getTransactDetails();
        this.transactDate = account.getTransactDate();
        this.transactType = account.getTransactType();
        this.transactor = account.getTransactor();
        this.transactorId = account.getTransactorId();
        this.transactorType = account.getTransactorType();
        this.amount = account.getAmount();
    }


    public static List<Transaction> fromAccounts(List<Account> accounts) {
        List<Transaction> transactions = new ArrayList<>();
        for (Account account : accounts) {
            Transaction transaction = new Transaction(account);
            transactions.add(transaction);
        }
        return transactions;
    }
}