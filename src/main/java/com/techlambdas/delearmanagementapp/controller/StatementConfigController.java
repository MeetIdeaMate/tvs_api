package com.techlambdas.delearmanagementapp.controller;

import com.techlambdas.delearmanagementapp.model.StatementConfig;
import com.techlambdas.delearmanagementapp.request.StatementConfigReq;
import com.techlambdas.delearmanagementapp.response.AppResponse;
import com.techlambdas.delearmanagementapp.service.StatementConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/statementConfig")
public class StatementConfigController {

    @Autowired
    private StatementConfigService statementConfigService;

    @PostMapping
    public ResponseEntity createConfigController(@RequestBody StatementConfigReq statementConfigReq){
      StatementConfig statementConfig = statementConfigService.createStatementConfig(statementConfigReq);
        return AppResponse.successResponse( HttpStatus.CREATED,"statementConfig", statementConfig );
    }
    @PutMapping("/{statementConfigId}")
    public ResponseEntity updateConfigController(@RequestBody StatementConfigReq statementConfigReq, @PathVariable String statementConfigId){
        StatementConfig statementConfig = statementConfigService.updateStatementConfig(statementConfigReq,statementConfigId);
        return AppResponse.successResponse( HttpStatus.OK,"statementConfig", statementConfig );
    }
    @GetMapping
    public ResponseEntity getAllStatementConfig(){
        return AppResponse.successResponse( HttpStatus.OK,"statementConfig", statementConfigService.getAllConfig());
    }

    @GetMapping("/{statementConfigId}")
    public ResponseEntity getAllStatementConfigById(@PathVariable String statementConfigId){
        return AppResponse.successResponse(HttpStatus.OK,"statementConfig", statementConfigService.getByStatementConfigId(statementConfigId));
    }

    @DeleteMapping("/{statementConfigId}")
    public ResponseEntity deleteStatementConfigId(@PathVariable String statementConfigId){
        statementConfigService.deleteStatementConfig(statementConfigId);
        return ResponseEntity.ok("statement Config deleted");
    }

}
