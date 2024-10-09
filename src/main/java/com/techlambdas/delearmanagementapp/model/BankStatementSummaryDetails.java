
package com.techlambdas.delearmanagementapp.model;
import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.model.submodel.StatementDetails;
import lombok.Data;
import java.util.List;
@Data
public class BankStatementSummaryDetails {
    private String description;
    private AccountType accountType;
    private double amount;
    private String AccountHeadName;
    private double ApplicationAmt;
    private List<StatementDetails> summaryDetails;
    private boolean isMissMatch;
}