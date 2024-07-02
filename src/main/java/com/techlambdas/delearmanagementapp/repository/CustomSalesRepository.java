package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.PaymentStatus;
import com.techlambdas.delearmanagementapp.model.Purchase;
import com.techlambdas.delearmanagementapp.model.Sales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CustomSalesRepository {
    List<Sales> getAllSales(String invoiceNo, String customerName, String mobileNo, String partNo, String paymentType, Boolean isCancelled, PaymentStatus paymentStatus,String billType);


    Page<Sales> getAllSalesWithPage(String invoiceNo, String categoryName,String customerName,String mobileNo,String partNo,String paymentType,Boolean isCancelled,String branchName,String branchId,String billType,PaymentStatus paymentStatus,LocalDate fromDate , LocalDate toDate, Pageable pageable);

}
