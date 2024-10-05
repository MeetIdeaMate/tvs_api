package com.techlambdas.delearmanagementapp.request;

import lombok.Data;

import java.util.List;

@Data
public class StatementConfigReq {

    private String statementConfigId;
    private List<String> statementConfiguration;

}
