package com.techlambdas.delearmanagementapp.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.constant.PricingFormat;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountHeadRequest {
    private String accountHeadCode;
    private String accountHeadName;
    private AccountType accountType;
    private PricingFormat pricingFormat;
    private double minAmount;
    private double maxAmount;
    private boolean cashierOps;
    private String transferFrom;
    private String transferTo;
    private boolean needPrinting;
    private String printingTemplate;
    private String ptVariables;
    private boolean activeStatus;
}
