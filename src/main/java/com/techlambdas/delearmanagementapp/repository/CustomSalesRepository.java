package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Purchase;
import com.techlambdas.delearmanagementapp.model.Sales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CustomSalesRepository {
    List<Sales> getAllSales(String invoiceNo, String customerName, String mobileNo, String partNo, String paymentType);


    Page<Sales> getAllSalesWithPage(String invoiceNo, String categoryName,String customerName,String mobileNo,String partNo,String paymentType, LocalDate fromDate , LocalDate toDate, Pageable pageable);

}
