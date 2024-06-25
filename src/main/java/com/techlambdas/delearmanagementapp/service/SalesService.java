package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.model.Sales;
import com.techlambdas.delearmanagementapp.request.SalesRequest;
import com.techlambdas.delearmanagementapp.request.SalesUpdateReq;
import com.techlambdas.delearmanagementapp.response.SalesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;


public interface SalesService {

    SalesResponse createSales(SalesRequest salesRequest);

    List<SalesResponse> getAllSales(String invoiceNo);

     Sales updateSales(SalesUpdateReq salesUpdateReq);

     void deleteSales(String invoiceNo);

     SalesResponse getSalesByInvoiceNo(String invoiceNo);


    List<SalesResponse> getAllSalesView(String invoiceNo);


    Page<SalesResponse> getAllSalesWithPage(String invoiceNo, String categoryName, LocalDate fromDate , LocalDate toDate, Pageable pageable);
}
