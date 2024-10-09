package com.techlambdas.delearmanagementapp.controller;


import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.model.Account;
import com.techlambdas.delearmanagementapp.request.AccountRequest;
import com.techlambdas.delearmanagementapp.response.AccountDataSummary;
import com.techlambdas.delearmanagementapp.response.Balance;
import com.techlambdas.delearmanagementapp.response.Ledger;
import com.techlambdas.delearmanagementapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.techlambdas.delearmanagementapp.response.AppResponse.successResponse;


@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity createAccountEntry(@RequestBody AccountRequest accountRequest){
        Account account = accountService.createAccountEntry(accountRequest);
        return successResponse(HttpStatus.CREATED,"account",account);
    }

    @PutMapping("/{transacId}")
    public ResponseEntity updateAccountEntry(@PathVariable String transacId,@RequestBody AccountRequest accountRequest){
        Account account = accountService.updateAccountEntry(transacId,accountRequest);
        return successResponse(HttpStatus.CREATED,"account",account);
    }

    @GetMapping("/allacc")
    public ResponseEntity getAllAccountEntry(){
        List<Account> account = accountService.getAllAccountEntry();
        return successResponse(HttpStatus.OK,"account",account);
    }

    @GetMapping
    public ResponseEntity getByAccType(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(required = false) String financialYear,@RequestParam(required = false) String accountHeadCode,
                                       @RequestParam(required = false) String accountHeadName,@RequestParam(required = false) String transactorId,
                                       @RequestParam(required = false) AccountType transactType,@RequestParam(required = false) String transactorName, @RequestParam(required = false) String transactDesc,
                                       @RequestParam(required = false) String shortNotes, @RequestParam(required = false) String transactRefNo,
                                       @RequestParam(required = false) String transactDetails, @RequestParam(required = false) LocalDate transactDate,
                                       @RequestParam(value = "fromDate",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                       @RequestParam(value = "toDate",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate){

        Page<Account> accounts = accountService.getByAccType(page,size,financialYear,accountHeadCode,accountHeadName,transactorId,
                transactType,transactorName,transactDesc,shortNotes,transactRefNo,transactDetails,transactDate,fromDate,toDate);
        return successResponse(HttpStatus.OK,"account",accounts);

    }


    @GetMapping("/filter")
    public ResponseEntity getFilAcc(@RequestParam(value = "date",required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate transactDate)
    {
        List<Balance> account = accountService.getFilAcc(transactDate);
        return successResponse(HttpStatus.OK,"success",account);
    }

    @GetMapping("/closingBalance")
    public ResponseEntity closingAmount(@RequestParam(value = "date",required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate transactDate)
    {
        Balance balance = accountService.closingBalance(transactDate);
        return successResponse(HttpStatus.OK,"success",balance);
    }


    @GetMapping("/ledger")
    public ResponseEntity getledger(@RequestParam(value = "date",required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate transacdate)
    {
        Ledger ledgers = accountService.getledger(transacdate);
        return successResponse(HttpStatus.OK,"success",ledgers);
    }
    @GetMapping("/summary")
    public ResponseEntity getAccountSummary(@RequestParam String operatorCode){
        List<AccountDataSummary> summary = accountService.getAccountSummaryByCollectionOperator(operatorCode);
        return successResponse(HttpStatus.OK, "accountSummary", summary);
    }

    @GetMapping("/cashpointsummary")
    public ResponseEntity getCashpointSummary(){
        List<AccountDataSummary> summary = accountService.getAccountSummaryByAllCollectionOperator();
        return successResponse(HttpStatus.OK,"cashpointsummary",summary);
    }
}
