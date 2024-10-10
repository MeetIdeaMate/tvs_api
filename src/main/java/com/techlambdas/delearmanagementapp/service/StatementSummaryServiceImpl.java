package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.model.*;
import com.techlambdas.delearmanagementapp.model.submodel.BankTransaction;
import com.techlambdas.delearmanagementapp.model.submodel.StatementDetails;
import com.techlambdas.delearmanagementapp.repository.BankStatementSummaryRepository;
import com.techlambdas.delearmanagementapp.repository.StatementSummaryCustomRepo;
import com.techlambdas.delearmanagementapp.request.StatementSummaryUpdateReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.techlambdas.delearmanagementapp.utils.RandomIdGenerator.getRandomId;

@Service
public class StatementSummaryServiceImpl implements StatementSummaryService{

    @Autowired
    StatementService statementService;

    @Autowired
    BankStatementSummaryRepository bankStatementSummaryRepository;

    @Autowired
    StatementSummaryCustomRepo statementSummaryCustomRepo;

    @Autowired
    AccountHeadService accountHeadService;

    @Autowired
    AccountService accountService;





    @Override
    public void createStatementSummary(String statementId) {
        BankStatementSummary bankStatementSummary = new BankStatementSummary();
        Statement statement = statementService.getByStatementId(statementId);
        Map<String, List<BankTransaction>> groupedTransactions = statement.getTransactions().stream()
                .collect(Collectors.groupingBy(transaction -> {
                    AccountType accountType = transaction.getCredit() > 0 ? AccountType.CREDIT : AccountType.DEBIT;
                    return accountType.name() + "-" + transaction.getPaymentType();
                }));
        List<BankStatementSummaryDetails> bankStatementSummaryList = new ArrayList<>();

        for (Map.Entry<String, List<BankTransaction>> entry : groupedTransactions.entrySet()) {
            String key = entry.getKey();
            List<BankTransaction> transactionList = entry.getValue();
            Map<String,Account> AccountMap = new HashMap<>();
            String[] keyParts = key.split("-");
            AccountType accountType = AccountType.valueOf(keyParts[0]);
            String paymentType = keyParts[1];
            BankStatementSummaryDetails BankStatementSummaryDetails = new BankStatementSummaryDetails();
            double cumulativeAmount = 0.0;
            double cumulativeApplicationAmt = 0.0;
            List<StatementDetails> StatementDetailsList = new ArrayList<>();
            List<StatementDetails> missMatchAccountDetails = new ArrayList<>();

            Optional<AccountHead> accountHead = Optional.ofNullable(accountHeadService.getAccountByAccountTypeAndAccountHeadName(accountType, paymentType));
            if(accountHead.isPresent()) {
                String accountHeadCode = accountHead.get().getAccountHeadCode();
                List<Account> accounts = accountService.getAllAccountWithFilter(statement.getFromDate(), statement.getToDate(),accountHeadCode);
                for(Account account: accounts){
                    AccountMap.put(account.getTransacId(), account);
                }
            }
            for (BankTransaction transaction : transactionList) {
                double transactionAmount = transaction.getCredit() > 0 ? transaction.getCredit() : transaction.getDebit();
                cumulativeAmount += transactionAmount;
                StatementDetails StatementDetailsRes = new StatementDetails();
                StatementDetailsRes.setSummaryId(getRandomId());
                StatementDetailsRes.setDescription(transaction.getDescription());
                StatementDetailsRes.setDate(transaction.getDate());
                StatementDetailsRes.setAmount(transactionAmount);
                Iterator<Map.Entry<String, Account>> iterator = AccountMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Account> accountEntry = iterator.next();
                    Account account = accountEntry.getValue();
                    if (transaction.getDate().equals(account.getTransactDate())
                            && transaction.getPaymentType().equals(account.getAccountHeadName())
                            && transaction.getDescription().contains(account.getTransactRefNo())) {
                        StatementDetailsRes.setApplicationAmt(account.getAmount());
                        cumulativeApplicationAmt += account.getAmount();
                        StatementDetailsRes.setPartyName(account.getTransactorId());
                        StatementDetailsRes.setSalesBillNo(account.getTransactDesc());
                        StatementDetailsRes.setApplicationTransactRefId(account.getTransactRefNo());
                        StatementDetailsRes.setMissMatch((transaction.getCredit() > 0 ? transaction.getCredit() : transaction.getDebit()) != account.getAmount());
                        iterator.remove(); 
                    }else{
                        StatementDetailsRes.setMissMatch(true);
                    }
                }
                StatementDetailsList.add(StatementDetailsRes);
                }
            System.out.println("account Map" +AccountMap);
            for (Map.Entry<String, Account> accountEntry : AccountMap.entrySet()) {
                System.out.println("account entry" +accountEntry);
                Account account = accountEntry.getValue();
                StatementDetails missMatchAccount = new StatementDetails();
                missMatchAccount.setSummaryId(getRandomId());
                cumulativeApplicationAmt += account.getAmount();
                missMatchAccount.setDate(account.getTransactDate());
                missMatchAccount.setAccountHead(account.getAccountHeadName());
                missMatchAccount.setApplicationAmt(account.getAmount());
                missMatchAccount.setPartyName(account.getTransactorId());
                missMatchAccount.setSalesBillNo(account.getTransactDesc());
                missMatchAccount.setApplicationTransactRefId(account.getTransactRefNo());
                missMatchAccount.setMissMatch(true);
                missMatchAccountDetails.add(missMatchAccount);
            }
            StatementDetailsList.addAll(missMatchAccountDetails);
                BankStatementSummaryDetails.setDescription("Summary for " + paymentType);
                BankStatementSummaryDetails.setAmount(cumulativeAmount);
                BankStatementSummaryDetails.setAccountHeadName(paymentType);
                BankStatementSummaryDetails.setApplicationAmt(cumulativeApplicationAmt);
                BankStatementSummaryDetails.setSummaryDetails(StatementDetailsList);
                BankStatementSummaryDetails.setAccountType(accountType);
                BankStatementSummaryDetails.setMissMatch(cumulativeAmount != cumulativeApplicationAmt);
                bankStatementSummaryList.add(BankStatementSummaryDetails);
            }
            bankStatementSummary.setBankStatementSummaryList(bankStatementSummaryList);
            bankStatementSummary.setStatementId(statementId);
            bankStatementSummaryRepository.save(bankStatementSummary);
        }

    @Override
    public BankStatementSummary getStatementSummary(String statementId, AccountType accountType, String accountHead) {
        return statementSummaryCustomRepo.getStatementSummary(accountType,accountHead,statementId);
    }

    @Override
    public BankStatementSummary getStatementSummaryById(String statementId) {
        return bankStatementSummaryRepository.findByStatementId(statementId);
    }

    @Override
    public void updateStatementSummary(String statementId, StatementSummaryUpdateReq statementSummaryUpdateReq) {

        BankStatementSummary bankStatementSummary = bankStatementSummaryRepository.findByStatementId(statementId);
        if (bankStatementSummary == null)
            throw new DataNotFoundException("Statement Summary Not Found with Id: " + statementId);

        Optional<StatementDetails> matchingSummary = bankStatementSummary.getBankStatementSummaryList().stream()
                .flatMap(bankSummary -> bankSummary.getSummaryDetails().stream())
                .filter(summary -> summary.getSummaryId().equals(statementSummaryUpdateReq.getSummaryId()))
                .findFirst();

        if (matchingSummary.isPresent()) {
            StatementDetails summary = matchingSummary.get();
            if(!summary.isMissMatch()){

            summary.setDescription("UPI PAYMENT : " + summary.getApplicationTransactRefId());
            if (statementSummaryUpdateReq.getAmount() == summary.getApplicationAmt()) {
                summary.setAmount(statementSummaryUpdateReq.getAmount());
            } else {
                throw new DataNotFoundException("Application amount Not Match By Statement Amount");
            }
            summary.setMissMatch(false);
            summary.setUpdated(true);
            bankStatementSummaryRepository.save(bankStatementSummary);
        } else {
            throw new DataNotFoundException("Summary not found with summaryId: " + statementSummaryUpdateReq.getSummaryId());
        }}
    }}
