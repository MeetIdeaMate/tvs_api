package com.techlambdas.delearmanagementapp.request;
import lombok.Data;
@Data
public class StatementSummaryUpdateReq {
    private String summaryId;
    private String checkNo;
    private double amount;
}
