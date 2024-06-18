package com.techlambdas.delearmanagementapp.request;

import com.techlambdas.delearmanagementapp.constant.PaymentType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReceiptRequest {
    private LocalDate receiptDate;
    private String customerId;
    private double receivedAmount;
    private String partNo;
    private PaymentType paymentType;
}
