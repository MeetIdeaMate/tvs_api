package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Sales;

import java.util.List;

public interface CustomSalesRepository {

    List<Sales> getAllSales(String invoiceNo);
}
