package com.techlambdas.delearmanagementapp.model;
import lombok.Data;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "statement_summary")
@Data
public class BankStatementSummary {
        @Id
        private String id;
        private String statementId;
        private List<BankStatementSummaryDetails> bankStatementSummaryList;
}