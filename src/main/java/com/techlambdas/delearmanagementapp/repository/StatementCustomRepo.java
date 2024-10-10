package com.techlambdas.delearmanagementapp.repository;
import java.time.LocalDate;
public interface StatementCustomRepo {
    Boolean existStatementByDate(LocalDate fromDate,LocalDate toDate);
}
