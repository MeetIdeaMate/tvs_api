package com.techlambdas.delearmanagementapp.service;


import com.google.common.io.Files;
import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.constant.PricingFormat;
import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.exception.InvalidDataException;
import com.techlambdas.delearmanagementapp.model.Account;
import com.techlambdas.delearmanagementapp.model.AccountHead;
import com.techlambdas.delearmanagementapp.model.Statement;
import com.techlambdas.delearmanagementapp.model.User;
import com.techlambdas.delearmanagementapp.model.submodel.BankTransaction;
import com.techlambdas.delearmanagementapp.repository.AccountCustomRepo;
import com.techlambdas.delearmanagementapp.repository.AccountHeadRepository;
import com.techlambdas.delearmanagementapp.repository.AccountRepository;
import com.techlambdas.delearmanagementapp.repository.UserRepository;
import com.techlambdas.delearmanagementapp.request.AccountRequest;
import com.techlambdas.delearmanagementapp.response.AccountDataSummary;
import com.techlambdas.delearmanagementapp.response.Balance;
import com.techlambdas.delearmanagementapp.response.Ledger;
import com.techlambdas.delearmanagementapp.response.Transaction;
import com.techlambdas.delearmanagementapp.utils.FileUploadUtils;
import com.techlambdas.delearmanagementapp.utils.RandomIdGenerator;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AccountServiceImpl implements AccountService {

    @Value("${app.image.upload-dir:./bankstatement/pdffiles}")
    private String uploadPdfFile;

    Function<AccountRequest, Account> convetToAccount = new Function<AccountRequest, Account>() {

        @Override
        public Account apply(AccountRequest accReq) {
            Account account = new Account();
            account.setFinancialYear(accReq.getFinancialYear());
            account.setAccountHeadCode(accReq.getAccountHeadCode());
            account.setTransactorId(accReq.getTransactorId());
            account.setTransactor(accReq.getTransactorName());
            account.setAmount(accReq.getAmount());
            account.setShortNotes(accReq.getShortNotes());
            account.setTransactDesc(accReq.getTransactDesc());
            account.setTransactDetails(accReq.getTransactDetails());
            account.setTransactDate(accReq.getTransactDate());
            account.setTransactRefNo(accReq.getTransactRefNo());
            return account;
        }
    };


    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountCustomRepo accountCustomRepo;

    @Autowired
    private AccountHeadRepository accountHeadRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public Balance closingBalance(LocalDate transactDate) {
        Balance balance = accountCustomRepo.closingBalance(transactDate);
        return balance;
    }

    public Account createSettlementEntry(AccountRequest accountRequest,
                                         boolean recpay, String settlementId) {
        try {
            AccountHead accountHead = accountHeadRepository.findByAccountHeadCode(accountRequest.getAccountHeadCode());
            if (accountHead == null)
                throw new DataNotFoundException("AccountHead not matched");

            Account account = convetToAccount.apply(accountRequest);
            account.setAccountHeadName(accountHead.getAccountHeadName());
            account.setTransacId(RandomIdGenerator.getRandomId());
            if (accountHead.getAccountType() == AccountType.CREDIT) {
                account.setTransactType(AccountType.CREDIT);
                account.setTransactorType("CollectionOperator");
            }
            account.setOnRecPayOnly(recpay);
            account.setCancelled(false);
            account.setEdited(false);
            account.setPrinted(false);
            account.setTransactRefNo(settlementId);
            Account acc = accountRepository.save(account);
            return account;
        } catch (DataNotFoundException ex) {
            throw new DataNotFoundException("Data not found --" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }

    }

    public Account generateAccountList(AccountRequest accountRequest) {
        try {
            AccountHead accountHead = accountHeadRepository.findByAccountHeadCode(accountRequest.getAccountHeadCode());
            if (accountHead == null)
                throw new DataNotFoundException("AccountHead not matched");

            Account account = convetToAccount.apply(accountRequest);
            account.setAccountHeadName(accountHead.getAccountHeadName());
            account.setTransacId(RandomIdGenerator.getRandomId());
            if (accountHead.getAccountType() == AccountType.CREDIT) {
                account.setTransactType(AccountType.CREDIT);
                account.setTransactorType("CollectionOperator");
            }
            account.setOnRecPayOnly(true);
            account.setCancelled(false);
            account.setEdited(false);
            account.setPrinted(false);
            return account;
        } catch (DataNotFoundException ex) {
            throw new DataNotFoundException("Data not found --" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }

    }

    @Override
    public List<Account> getAllAccountEntry() {
        return accountRepository.findAll();
    }

    @Override
    public Page<Account> getByAccType(int page, int size, String financialYear, String accountHeadCode, String accountHeadName, String transactorId, AccountType transactType, String transactorName, String transactDesc, String shortNotes, String transactRefNo, String transactDetails, LocalDate transactDate, LocalDate fromDate, LocalDate toDate) {
        Page<Account> accounts = accountCustomRepo.getByAccType(page, size, financialYear, accountHeadCode, accountHeadName, transactorId, transactType, transactorName, transactDesc, shortNotes, transactRefNo, transactDetails, transactDate, fromDate, toDate);
        return accounts;
    }

    @Override
    public List<Balance> getFilAcc(LocalDate transactDate) {
        List<Balance> openingBalance = accountCustomRepo.filterAcc(transactDate);
        return openingBalance;
    }


    @Override
    public Ledger getledger(LocalDate transacdate) {
        Ledger ledger = new Ledger();
        ledger.setLedgerDate(LocalDate.now());

        List<Balance> openingBalances = getFilAcc(transacdate);
        double openingBalanceSum = 0;
        for (Balance openingBalance : openingBalances) {
            openingBalanceSum += openingBalance.getBalanceAmount();
        }
        ledger.setOpeningBalance(openingBalanceSum);
        //    List<Account> accounts = accountRepository.findByTransactDateBetween(transacdate, LocalDate.now());
        List<Account> accounts = accountCustomRepo.findByTransactDateBetween(transacdate, LocalDate.now());
        ledger.setTransaction(Transaction.fromAccounts(accounts));

        Balance cb = closingBalance(transacdate);
        if (cb == null) {
            ledger.setClosingBalance(ledger.getOpeningBalance());
        } else {
            double closingBalance = cb.getClosingBalance();
            ledger.setClosingBalance(openingBalanceSum + closingBalance);
        }
        return ledger;
    }


    @Override
    public Account createAccountEntry(AccountRequest accountRequest) {
        try {
            System.out.println("Account head code ::" + accountRequest.getAccountHeadCode());
            AccountHead accountHead = accountHeadRepository.findByAccountHeadCode(accountRequest.getAccountHeadCode());
            if (accountHead == null)
                throw new DataNotFoundException("AccountHead not matched");
            Account account = convetToAccount.apply(accountRequest);
            account.setAccountHeadName(accountHead.getAccountHeadName());
            account.setTransacId(RandomIdGenerator.getRandomId());
            if (accountHead.getPricingFormat() == PricingFormat.FLAT)
                if (accountHead.getMaxAmount() < account.getAmount())
                    throw new InvalidDataException("Amount Mismatched according to AccountHead");
            //Outgoing means payments
            if (accountHead.getAccountType() == AccountType.DEBIT) {
                account.setTransactType(AccountType.DEBIT);
            }
            //Incoming means receipt
            else if (accountHead.getAccountType() == AccountType.CREDIT) {
                account.setTransactType(AccountType.CREDIT);
                if (accountHead.getTransferFrom().equalsIgnoreCase("User")) {
                    User user = userRepository.findUserByUserId(account.getTransactorId());
                    if (user == null)
                        throw new DataNotFoundException("userId Not valid");
                    account.setTransactor(user.getUserName());
                    account.setTransactorType("User");
                }
            }
            if (accountHead.isCashierOps())
                account.setOnRecPayOnly(true);
            account.setCancelled(false);
            account.setEdited(false);
            if (accountHead.isNeedPrinting())
                account.setPrinted(true);
            Account acc = accountRepository.save(account);
            return account;
        }catch (DataNotFoundException ex) {
            throw new DataNotFoundException("Data not found --" + ex.getMessage());
        }catch (InvalidDataException ex) {
            throw new InvalidDataException("Data Invalid --" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }

    }

    @Override
    public Account updateAccountEntry(String transacId, AccountRequest accountRequest) {
        try {
            Account editableAccount = accountRepository.findByTransacId(transacId);
            if (editableAccount == null)
                throw new DataNotFoundException("Account Entry Not Valid");
            editableAccount.setEdited(true);
            editableAccount.setCancelled(true);
            accountRepository.save(editableAccount);
            String editedinfo = "Edited on id:" + editableAccount.getTransacId();
            if (accountRequest.getTransactDetails() == null)
                accountRequest.setTransactDetails(editedinfo);
            else
                accountRequest.setTransactDetails(editedinfo + " " + accountRequest.getTransactDetails());
            Account newEntry = createAccountEntry(accountRequest);
            return newEntry;
        } catch (DataNotFoundException ex) {
            throw new DataNotFoundException("Data not found --" + ex.getMessage());
        } catch (InvalidDataException ex) {
            throw new InvalidDataException("Data Invalid --" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public List<AccountDataSummary> getAccountSummaryByCollectionOperator(String operatorCode) {
        return accountCustomRepo.getAggregateResultByOperator(operatorCode);
    }

    @Override
    public List<AccountDataSummary> getAccountSummaryByAllCollectionOperator() {
        return accountCustomRepo.getAggregateResultByAllOperator();
    }


    @Override
    public Statement uploadFile(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String originalFileName = FilenameUtils.removeExtension(file.getOriginalFilename());
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String filename = originalFileName + "_" + currentDateTime + "." + extension;

        try {
            FileUploadUtils.uploadFiletoVps(uploadPdfFile, filename, file);
        } catch (IOException e) {
            throw new RuntimeException("Error uploading file", e);
        }

        return readTxtFile(filename);
    }

    private Statement readTxtFile(String fileName) {
        String filePath = uploadPdfFile + "/" + fileName;
        File pdfFile = new File(filePath);
        StringBuilder allPages = new StringBuilder();

        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(true);
            Rectangle rect = new Rectangle(0, 0, 2200, 2200);
            stripper.addRegion("region", rect);

            for (int page = 0; page < document.getNumberOfPages(); page++) {
                stripper.extractRegions(document.getPage(page));
                allPages.append(stripper.getTextForRegion("region"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parseContent(allPages.toString());
    }

    private Statement parseContent(String content) {
        Statement statement = new Statement();
        List<BankTransaction> transactions = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Define patterns
        Pattern accountNumberPattern = Pattern.compile("ACCOUNT NO\\s+:([A-Z]+-\\d+)");
        Pattern customerNamePattern = Pattern.compile("CUSTOMER DETAILS\\s*:(.+)");
        Pattern openingBalancePattern = Pattern.compile("Opening Balance\\s+([\\d,\\.]+)");
        Pattern closingBalancePattern = Pattern.compile("Closing Balance\\s+([\\d,\\.]+)");
        Pattern transactionPattern = Pattern.compile(
                "(\\d{2}/\\d{2}/\\d{4})\\s*" +          // Date
                        "(.*?)\\s*" +                          // Description
                        "([\\d,]+\\.\\d{2})\\s*" +             // Credit amount
                        "([\\d,\\.\\-]+)"
        );

        // Match and set statement properties
        System.out.println("Pattern: " + accountNumberPattern);

        Matcher matcher = accountNumberPattern.matcher(content);
        System.out.println("matcher: " + matcher);


        if (matcher.find()) statement.setAccountNumber(matcher.group(1));


        matcher = customerNamePattern.matcher(content);
        if (matcher.find()) statement.setCustomerName(matcher.group(1).trim());

        matcher = openingBalancePattern.matcher(content);
        if (matcher.find()) statement.setOpeningBalance(Double.parseDouble(matcher.group(1).replace(",", "")));

        // Merge transaction lines
        List<String> mergedTransactions = mergeTransactionLines(content.split("\\r?\\n"));
        double prevBalance = statement.getOpeningBalance();

        for (String line : mergedTransactions) {
            System.out.println("Processing line: " + line);
            matcher = transactionPattern.matcher(line);
            if (matcher.find()) {
                LocalDate date = LocalDate.parse(matcher.group(1), formatter);
                String description = matcher.group(2).trim();
                double amount = Double.parseDouble(matcher.group(3).replace(",", ""));
                double balance = Double.parseDouble(matcher.group(4).replace(",", ""));
                double transactAmount = balance - prevBalance;

                BankTransaction transaction = new BankTransaction();
                transaction.setDate(date);
                transaction.setDescription(description);
                transaction.setBalance(balance);
                if (transactAmount > 0)
                    transaction.setCredit(amount);
                else
                    transaction.setDebit(amount);

                prevBalance = balance;

                String paymentType = determinePaymentType(description);
                transaction.setPaymentType(paymentType);
                transactions.add(transaction);
            } else {
                System.out.println("Unmatched line: " + line);
            }
        }


        matcher = closingBalancePattern.matcher(content);
        if (matcher.find()) statement.setClosingBalance(Double.parseDouble(matcher.group(1).replace(",", "")));

        statement.setTransactions(transactions);
        if (!transactions.isEmpty()) {
            statement.setFromDate(transactions.get(0).getDate());
            statement.setToDate(transactions.get(transactions.size() - 1).getDate());
        }

        return statement;
    }

    private List<String> mergeTransactionLines(String[] lines) {
        List<String> mergedTransactions = new ArrayList<>();
        StringBuilder currentTransaction = new StringBuilder();

        for (String line : lines) {
            if (line.matches("^\\d{2}/\\d{2}/\\d{4}.*")) {
                if (currentTransaction.length() > 0) {
                    mergedTransactions.add(currentTransaction.toString().replaceAll("\\s+", " "));
                    currentTransaction.setLength(0);
                }
                currentTransaction.append(line);
            } else {
                currentTransaction.append(" ").append(line);
            }
        }

        if (currentTransaction.length() > 0) {
            mergedTransactions.add(currentTransaction.toString().replaceAll("\\s+", " "));
        }

        return mergedTransactions;
    }

    private String determinePaymentType(String description) {
        description = description.toLowerCase();

        if (description.contains("atm")) {
            return "ATM";
        } else if (description.contains("neft")) {
            return "NEFT";
        } else if (description.contains("imps")) {
            return "UPI";
        } else if (description.contains("chq")) {
            return "CHEQUE";
        } else if (description.contains("pos") || description.contains("swipe")) {
            return "CARD";
        } else if (description.contains("upi") || description.contains("gpay") || description.contains("phonepe")) {
            return "UPI";
        } else {
            return "Unknown";
        }
    }
}