package com.techlambdas.delearmanagementapp.controller;

import com.techlambdas.delearmanagementapp.model.BankStatementSummary;
import com.techlambdas.delearmanagementapp.response.AppResponse;
import com.techlambdas.delearmanagementapp.service.StatementSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statementSummary")
public class StatementSummaryController {

    @Autowired
    private StatementSummaryService statementSummaryService;

    @GetMapping
    public ResponseEntity getStatementSummary(@RequestParam String statementId){
        BankStatementSummary bankStatementSummary = statementSummaryService.getStatementSummary(statementId);

        return AppResponse.successResponse(HttpStatus.OK,"bankStatementSummary",bankStatementSummary);
    }
}
