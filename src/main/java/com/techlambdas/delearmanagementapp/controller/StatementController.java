package com.techlambdas.delearmanagementapp.controller;
import com.techlambdas.delearmanagementapp.model.Statement;
import com.techlambdas.delearmanagementapp.response.AppResponse;
import com.techlambdas.delearmanagementapp.service.StatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/statement")
public class StatementController {
    @Autowired
    StatementService statementService;

    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestPart("file") MultipartFile file) {
        Statement response= statementService.uploadFile(file);
        return AppResponse.successResponse(HttpStatus.OK,"statement", response);
    }
    @GetMapping
    public ResponseEntity getAllStatement() {
        List<Statement> statement= statementService.getAllStatement();
        return AppResponse.successResponse(HttpStatus.OK,"statement", statement);
    }


    @GetMapping("/{statementId}")
    public ResponseEntity getStatementById(@PathVariable String statementId, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate ){
        Statement statement= statementService.getByStatementId(statementId,fromDate,toDate);
        return AppResponse.successResponse(HttpStatus.OK,"statement", statement);
    }

    @GetMapping("/fileInfo")
    public ResponseEntity getAllStatementFileInfo(){
        return AppResponse.successResponse(HttpStatus.OK,"StatementList",statementService.getStatementFileInfo());
    }
}
