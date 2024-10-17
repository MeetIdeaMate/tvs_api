package com.techlambdas.delearmanagementapp.controller;

import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.model.BankStatementSummary;
import com.techlambdas.delearmanagementapp.request.StatementSummaryUpdateReq;
import com.techlambdas.delearmanagementapp.response.AppResponse;
import com.techlambdas.delearmanagementapp.service.StatementSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/statementSummary")
public class StatementSummaryController {

    @Autowired
    private StatementSummaryService statementSummaryService;

    @GetMapping
    public ResponseEntity getStatementSummary(@RequestParam String statementId, @RequestParam(required = false) AccountType accountType, @RequestParam(required = false) String accountHead, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate){
        BankStatementSummary bankStatementSummary = statementSummaryService.getStatementSummary(statementId,accountType,accountHead,fromDate ,toDate );
        return AppResponse.successResponse(HttpStatus.OK,"bankStatementSummary",bankStatementSummary);
    }

    @PatchMapping("{statementId}")
    public ResponseEntity updateStatementSummary(@PathVariable String statementId,@RequestBody StatementSummaryUpdateReq statementSummaryUpdateReq){
        BankStatementSummary bankStatementSummary= statementSummaryService.updateStatementSummary(statementId,statementSummaryUpdateReq);
        return AppResponse.successResponse(HttpStatus.OK,"bankStatementSummary",bankStatementSummary);

    }
}
