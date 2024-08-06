package com.techlambdas.delearmanagementapp.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Ledger {
   private LocalDate ledgerDate;
   private double openingBalance;
   private List<Transaction> transaction;
   private double closingBalance;

}