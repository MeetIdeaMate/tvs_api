package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.model.PaidDetail;
import com.techlambdas.delearmanagementapp.model.PaymentStatus;
import com.techlambdas.delearmanagementapp.model.Sales;
import com.techlambdas.delearmanagementapp.request.PaidDetailReq;
import com.techlambdas.delearmanagementapp.request.SalesRequest;
import com.techlambdas.delearmanagementapp.request.SalesUpdateReq;
import com.techlambdas.delearmanagementapp.response.SalesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;


public interface SalesService {

    SalesResponse createSales(SalesRequest salesRequest);

    List<SalesResponse> getAllSales(String invoiceNo, String customerName, String mobileNo, String partNo, String paymentType, Boolean isCancelled, PaymentStatus paymentStatus,String billType);

     Sales updateSales(SalesUpdateReq salesUpdateReq);

     void deleteSales(String invoiceNo);

     SalesResponse getSalesByInvoiceNo(String invoiceNo);


//    List<SalesResponse> getAllSalesView(String invoiceNo);


    Page<SalesResponse> getAllSalesWithPage(String invoiceNo, String categoryName,String customerName,String mobileNo,String partNo,String paymentType,Boolean isCancelled,String branchName,String billType,PaymentStatus paymentStatus,LocalDate fromDate , LocalDate toDate, Pageable pageable);

    String updatePaymentDetails(String salesId, PaidDetailReq paidDetailReq);

    String cancelPaymentDetails(String salesId, String paymentId,String reason);

    String cancelSales(String salesId, String reason);
}
