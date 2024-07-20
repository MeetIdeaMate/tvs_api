package com.techlambdas.delearmanagementapp.response;

import lombok.Data;
import java.util.List;

@Data
public class SalesReport {
 private List<SalesDetail>salesDetails;
 private double totalSalesAmount;
}
