package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.model.*;
import com.techlambdas.delearmanagementapp.model.submodel.BankTransaction;
import com.techlambdas.delearmanagementapp.model.submodel.StatementDetails;
import com.techlambdas.delearmanagementapp.repository.BankStatementSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StatementSummaryServiceImpl implements StatementSummaryService{

    @Autowired
    StatementService statementService;

    @Autowired
    BankStatementSummaryRepository bankStatementSummaryRepository;

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
                    return accountType.name() + "-" + transaction.getPaymentType(); // Concatenate as key
                }));

        List<BankStatementSummaryDetails> bankStatementSummaryList = new ArrayList<>();

        for (Map.Entry<String, List<BankTransaction>> entry : groupedTransactions.entrySet()) {
            String key = entry.getKey();
            List<BankTransaction> transactionList = entry.getValue();

            String[] keyParts = key.split("-");
            AccountType accountType = AccountType.valueOf(keyParts[0]);
            String paymentType = keyParts[1];

            BankStatementSummaryDetails bankStatementSummaryDetails = new BankStatementSummaryDetails();
            double cumulativeAmount = 0.0;
            double cumulativeApplicationAmt = 0.0;
            List<StatementDetails> summaryDetailsList = new ArrayList<>();
            StatementDetails missMatchstatementDetails = new StatementDetails();

            for (BankTransaction transaction : transactionList) {
                double transactionAmount = transaction.getCredit() > 0 ? transaction.getCredit() : transaction.getDebit();
                cumulativeAmount += transactionAmount;
                StatementDetails summaryDetailsRes = new StatementDetails();
                summaryDetailsRes.setDescription(transaction.getDescription());
                summaryDetailsRes.setDate(transaction.getDate());
                summaryDetailsRes.setAmount(transactionAmount);
                List<AccountHead> accountHeads = accountHeadService.getAllAccHead(accountType);
                Optional<AccountHead> matchingAccountHeadOpt = accountHeads.stream()
                        .filter(accountHead -> accountHead.getAccountHeadName().equals(paymentType))
                        .findFirst();
                if (matchingAccountHeadOpt.isPresent()) {
                    AccountHead matchingAccountHead = matchingAccountHeadOpt.get();
                    summaryDetailsRes.setAccountHead(matchingAccountHead.getAccountHeadName());
                    String accountHeadCode = matchingAccountHead.getAccountHeadCode();
                    List<Account> accounts = accountService.findByTransactDateBetween(statement.getFromDate(), statement.getToDate());
                    System.out.println("accountsList" + accounts);
                    List<Account> matchingAccountOpt = accounts.stream()
                            .filter(acc -> acc.getAccountHeadCode().equals(accountHeadCode))
                            .collect(Collectors.toList());
                    System.out.println("MatchedAccount" + matchingAccountOpt);
                    if (!matchingAccountOpt.isEmpty()) {

                        for (Account MatchAcc : matchingAccountOpt) {
                            if (transaction.getDate().equals(MatchAcc.getTransactDate())
                                    && transaction.getPaymentType().equals(MatchAcc.getAccountHeadName())
                                    && transaction.getDescription().contains(MatchAcc.getTransactRefNo())) {
                                summaryDetailsRes.setApplicationAmt(MatchAcc.getAmount());
                                cumulativeApplicationAmt += MatchAcc.getAmount();
                                summaryDetailsRes.setPartyName(MatchAcc.getTransactorId());
                                summaryDetailsRes.setSalesBillNo(MatchAcc.getTransactRefNo());
                                summaryDetailsRes.setMissMatch((transaction.getCredit() > 0 ? transaction.getCredit() : transaction.getDebit()) != MatchAcc.getAmount());
                            } else {
                                missMatchstatementDetails.setDate(null);
                                missMatchstatementDetails.setAmount(0);
                                missMatchstatementDetails.setCheque(null);
                                missMatchstatementDetails.setAccountHead(matchingAccountHead.getAccountHeadName());
                                missMatchstatementDetails.setDescription(MatchAcc.getTransactDesc());
                                missMatchstatementDetails.setApplicationAmt(MatchAcc.getAmount());
                                missMatchstatementDetails.setPartyName(MatchAcc.getTransactorId());
                                missMatchstatementDetails.setSalesBillNo(MatchAcc.getTransactRefNo());
                                missMatchstatementDetails.setMissMatch((transaction.getCredit() > 0 ? transaction.getCredit() : transaction.getDebit()) != MatchAcc.getAmount());

                            }


                        } }else{


                            summaryDetailsRes.setApplicationAmt(0.0);

                        }

                    }

                    summaryDetailsList.add(summaryDetailsRes);


                }
            summaryDetailsList.add(missMatchstatementDetails);


                bankStatementSummaryDetails.setDescription("Summary for " + paymentType);
                bankStatementSummaryDetails.setAmount(cumulativeAmount);
                bankStatementSummaryDetails.setAccountHeadName(paymentType);
                bankStatementSummaryDetails.setApplicationAmt(cumulativeApplicationAmt);
                bankStatementSummaryDetails.setSummaryDetails(summaryDetailsList);
                bankStatementSummaryDetails.setAccountType(accountType);
                bankStatementSummaryDetails.setMissMatch(cumulativeAmount != cumulativeApplicationAmt);

                bankStatementSummaryList.add(bankStatementSummaryDetails);
            }

            bankStatementSummary.setBankStatementSummaryList(bankStatementSummaryList);
            bankStatementSummary.setStatementId(statementId);

            bankStatementSummaryRepository.save(bankStatementSummary);
        }


    @Override
    public BankStatementSummary getStatementSummary(String statementId) {
        return bankStatementSummaryRepository.findByStatementId(statementId);
    }


}
