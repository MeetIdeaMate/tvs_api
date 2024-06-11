package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Receipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CustomReceiptRepository {
    List<Receipt> getAllReceipts(String receiptId, LocalDate fromDate, LocalDate toDate);

    Page<Receipt> getAllReceiptsWithPage(String receiptId, LocalDate fromDate, LocalDate toDate, Pageable pageable);
}
