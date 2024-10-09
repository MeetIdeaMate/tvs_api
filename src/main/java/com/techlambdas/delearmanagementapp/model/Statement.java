package com.techlambdas.delearmanagementapp.model;
import com.techlambdas.delearmanagementapp.model.submodel.BankTransaction;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.List;
@Data
@Document(collection = "statements")
public class Statement {
    @Id
    private String id;
    private String statementId;
    private String accountNumber;
    private String customerName;
    private LocalDate fromDate;
    private LocalDate toDate;
    private double openingBalance;
    private List<BankTransaction> transactions;
    private double closingBalance;
    private String statementDate;
}