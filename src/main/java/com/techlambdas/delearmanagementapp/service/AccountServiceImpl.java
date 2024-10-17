package com.techlambdas.delearmanagementapp.service;


import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.constant.PricingFormat;
import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.exception.InvalidDataException;
import com.techlambdas.delearmanagementapp.model.*;
import com.techlambdas.delearmanagementapp.repository.AccountCustomRepo;
import com.techlambdas.delearmanagementapp.repository.AccountHeadRepository;
import com.techlambdas.delearmanagementapp.repository.AccountRepository;
import com.techlambdas.delearmanagementapp.repository.UserRepository;
import com.techlambdas.delearmanagementapp.request.AccountRequest;
import com.techlambdas.delearmanagementapp.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import static com.techlambdas.delearmanagementapp.utils.RandomIdGenerator.getRandomId;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private StatementConfigService statementConfigService;

    @Autowired
    private AccountHeadService accountHeadService;

    @Autowired
    private SalesService salesService;


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
            account.setTransacId(getRandomId());
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
            account.setTransacId(getRandomId());
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
    public List<Account> getAllAccountWithFilter(LocalDate fromDate, LocalDate toDate, String accountHeadCode) {
        return accountCustomRepo.getAllAccountByFilter(fromDate,toDate,accountHeadCode);
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
            account.setTransacId(getRandomId());
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

}