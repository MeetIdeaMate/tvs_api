package com.techlambdas.delearmanagementapp.request;

import com.techlambdas.delearmanagementapp.constant.PaymentType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {

    private LocalDate bookingDate;
    private String customerId;
    private String customerName;
    private String partNo;
    private String additionalInfo;
    private PaymentType paymentType;
    private double amount;
    private String excutiveId;
    private LocalDate targetInvoiceDate;
}