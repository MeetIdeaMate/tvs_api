package com.techlambdas.delearmanagementapp.response;

import com.techlambdas.delearmanagementapp.constant.PaymentType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReceiptResponse {
    private String receiptNo;
    private LocalDate receiptDate;
    private String customerId;
    private String customerName;
    private double receivedAmount;
    private String partNo;
    private String itemName;
    private PaymentType paymentType;
}
