package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.model.Receipt;
import com.techlambdas.delearmanagementapp.request.ReceiptRequest;
import com.techlambdas.delearmanagementapp.response.ReceiptResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface ReceiptService {
    Receipt createReceipt(ReceiptRequest receiptRequest);

    List<ReceiptResponse> getAllReceipts(String receiptId, LocalDate fromDate, LocalDate toDate);

    Receipt updateReceipt(String receiptId, ReceiptRequest receiptRequest);

    Page<ReceiptResponse> getAllReceiptsWithPage(String receiptId, LocalDate fromDate, LocalDate toDate, Pageable pageable);

    Receipt getReceiptByReceiptId(String receiptId);
}
