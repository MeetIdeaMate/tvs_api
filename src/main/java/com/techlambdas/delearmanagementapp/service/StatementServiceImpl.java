package com.techlambdas.delearmanagementapp.service;


import com.techlambdas.delearmanagementapp.model.*;
import com.techlambdas.delearmanagementapp.model.submodel.BankTransaction;
import com.techlambdas.delearmanagementapp.repository.StatementRepository;
import com.techlambdas.delearmanagementapp.utils.FileUploadUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.techlambdas.delearmanagementapp.model.Statement;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static com.techlambdas.delearmanagementapp.utils.RandomIdGenerator.getRandomId;
@Service
public class StatementServiceImpl implements StatementService {
    @Value("${app.image.upload-dir:./bankstatement/pdffiles}")
    private String uploadPdfFile;
    @Autowired
    private StatementConfigService statementConfigService;

    @Autowired
    private StatementRepository statementRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private  StatementSummaryService statementSummaryService;

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

    @Override
    public Statement getByStatementId(String statementId) {
        return statementRepository.findByStatementId(statementId);
    }

    @Override
    public List<Statement> getAllStatement() {
        return statementRepository.findAll();
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
        java.util.List<BankTransaction> transactions = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        Pattern accountNumberPattern = Pattern.compile("ACCOUNT NO\\s+:([A-Z]+-\\d+)");
        Pattern customerNamePattern = Pattern.compile("CUSTOMER DETAILS\\s*:(.+)");
        Pattern openingBalancePattern = Pattern.compile("Opening Balance\\s+([\\d,\\.]+)");
        Pattern closingBalancePattern = Pattern.compile("Closing Balance\\s+([\\d,\\.]+)");
        Pattern transactionPattern = Pattern.compile(
                "(\\d{2}/\\d{2}/\\d{4})\\s*" +
                        "(.*?)\\s*" +
                        "([\\d,]+\\.\\d{2})\\s*" +
                        "([\\d,\\.\\-]+)"
        );
        System.out.println("Pattern: " + accountNumberPattern);

        Matcher matcher = accountNumberPattern.matcher(content);
        System.out.println("matcher: " + matcher);


        if (matcher.find()) statement.setAccountNumber(matcher.group(1));


        matcher = customerNamePattern.matcher(content);
        if (matcher.find()) statement.setCustomerName(matcher.group(1).trim());

        matcher = openingBalancePattern.matcher(content);
        if (matcher.find()) statement.setOpeningBalance(Double.parseDouble(matcher.group(1).replace(",", "")));

        java.util.List<String> mergedTransactions = mergeTransactionLines(content.split("\\r?\\n"));
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
                transaction.setTransactionId(getRandomId());
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
        statement.setStatementId(getRandomId());
        Statement CreateStatement = statementRepository.save(statement);
        statementSummaryService.createStatementSummary(CreateStatement.getStatementId());
        return CreateStatement;
    }
    private java.util.List<String> mergeTransactionLines(String[] lines) {
        java.util.List<String> mergedTransactions = new ArrayList<>();
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
        java.util.List<StatementConfig> statementConfigList = statementConfigService.getAllConfig();
        return statementConfigList.stream()
                .filter(statementConfig -> statementConfig.getStatementConfiguration().stream()
                        .anyMatch(description::contains))
                .map(StatementConfig::getStatementConfigId)
                .findFirst()
                .orElse("Unknown");
    }
//    private void generateAccount(BankTransaction transaction) {
//        AccountRequest accountRequest = new AccountRequest();
//        AccountType accountType = transaction.getCredit() !=0 ? AccountType.CREDIT: AccountType.DEBIT;
//        List<AccountHead> accountHeads = accountHeadService.getAllAccHead(accountType);
//        System.out.println("account" + accountHeads);
//        String accountHeadCode = accountHeads.stream()
//                .filter(accountHead -> transaction.getPaymentType().equals(accountHead.getAccountHeadName()))
//                .map(AccountHead::getAccountHeadCode)
//                .findFirst()
//                .orElse("Unknown");
//        if ("Unknown".equals(accountHeadCode)) {
//            System.out.println("Skipping account creation: No matching account head for transaction: " + transaction.getDescription());
//            return; // Skip account creation
//        }else{
//            System.out.println("matched account creation:  matching account head for transaction: " + transaction.getDescription() + accountHeadCode);
//        }
//
//        accountRequest.setAccountHeadCode(accountHeadCode);
//
//        if (transaction.getCredit() > 0) {
//            accountRequest.setAmount(transaction.getCredit());
//        } else if (transaction.getDebit() > 0) {
//            accountRequest.setAmount(transaction.getDebit());
//        }
//        accountRequest.setTransactRefNo(transaction.getTransactionId());
//        accountRequest.setTransactDesc(transaction.getDescription());
//        accountRequest.setTransactDate(transaction.getDate());
//        //  accountRequest.setTransactorId(JwtUtils.getUserIdFromToken().get());
//        createAccountEntry(accountRequest);
//    }
//



}



//  for (SalesResponse sale : salesList) {
//        for (PaidDetail paidDetail : sale.getPaidDetails()) {
//        if (!paidDetail.isCancelled()) {
//        if (account.getAccountHeadName() != null && paidDetail.getPaymentType() != null) {
//        if (account.getAccountHeadName().equals(paidDetail.getPaymentType().name()) &&
//        paidDetail.getPaymentDate().isEqual(account.getTransactDate()) && paidDetail.getPaymentReference().contains(account.getTransactDesc())  )  {
//applicationAmt += paidDetail.getPaidAmount();
//                                    summaryDetailsRes.setSalesBillNo(sale.getInvoiceNo());
//        summaryDetailsRes.setPartyName(sale.getCustomerName());
//        }
//        }
//        }
//        }
//        }