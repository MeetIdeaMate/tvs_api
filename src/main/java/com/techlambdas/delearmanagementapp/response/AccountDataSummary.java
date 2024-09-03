package com.techlambdas.delearmanagementapp.response;

import lombok.Data;

import java.util.List;

@Data
public class AccountDataSummary {
    private String operatorCode;
    private List<AccountSummary> summary;

}
