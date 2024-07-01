package com.techlambdas.delearmanagementapp.request;

import com.techlambdas.delearmanagementapp.constant.PaymentType;
import com.techlambdas.delearmanagementapp.model.PaidDetail;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {
    private LocalDate bookingDate;
    private String customerId;
    private String partNo;
    private String additionalInfo;
    private PaidDetail paidDetail;
    private String executiveId;
    private LocalDate targetInvoiceDate;
    private String branchId;
}