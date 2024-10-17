package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.model.StatementConfig;
import com.techlambdas.delearmanagementapp.request.StatementConfigReq;

import java.util.List;

public interface StatementConfigService {

    StatementConfig createStatementConfig(StatementConfigReq statementConfigReq);

    StatementConfig updateStatementConfig(StatementConfigReq statementConfigReq,String statementConfigId);

    List<StatementConfig> getAllConfig();

    StatementConfig getByStatementConfigId(String statementConfigId);

    void deleteStatementConfig(String statementConfigId);

}
