package com.techlambdas.delearmanagementapp.controller;
import com.techlambdas.delearmanagementapp.model.Statement;
import com.techlambdas.delearmanagementapp.response.AppResponse;
import com.techlambdas.delearmanagementapp.response.StatementFileDetailsRes;
import com.techlambdas.delearmanagementapp.service.StatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
@RestController
@RequestMapping("/statement")
public class StatementController {
    @Autowired
    StatementService statementService;

    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestPart("file") MultipartFile file) {
        Statement response= statementService.uploadFile(file);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity getAllStatement() {
        List<Statement> statement= statementService.getAllStatement();
        return AppResponse.successResponse(HttpStatus.OK,"statement", statement);
    }


    @GetMapping("/{statementId}")
    public ResponseEntity getStatementById(@PathVariable String statementId){
        Statement statement= statementService.getByStatementId(statementId);
        return AppResponse.successResponse(HttpStatus.OK,"statement", statement);
    }

    @GetMapping("/fileInfo")
    public ResponseEntity getAllStatementFileInfo(){
        return AppResponse.successResponse(HttpStatus.OK,"StatementList",statementService.getStatementFileInfo());
    }
}
