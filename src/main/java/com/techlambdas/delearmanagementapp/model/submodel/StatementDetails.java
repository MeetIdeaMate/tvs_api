package com.techlambdas.delearmanagementapp.model.submodel;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StatementDetails {
    private String summaryId;
    private String Description;
    private LocalDate date;
    private String cheque;
    private double amount;
    private String salesBillNo;
    private String partyName;
    private String accountHead;
    private double applicationAmt;
    private String applicationTransactRefId;
    private boolean isMissMatch;
    private boolean isUpdated;
}
