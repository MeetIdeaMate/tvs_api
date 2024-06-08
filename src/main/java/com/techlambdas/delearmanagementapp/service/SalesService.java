package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.model.Sales;
import com.techlambdas.delearmanagementapp.request.SalesRequest;
import com.techlambdas.delearmanagementapp.request.SalesUpdateReq;
import com.techlambdas.delearmanagementapp.response.SalesResponse;
import org.springframework.stereotype.Service;

import java.util.List;


public interface SalesService {

    Sales createSales(SalesRequest salesRequest);

    List<Sales> getAllSales();

     Sales updateSales(SalesUpdateReq salesUpdateReq);

     void deleteSales(String invoiceNo);

     Sales getSalesByInvoiceNo(String invoiceNo);


    List<SalesResponse> getAllSalesView(String invoiceNo);




}
