package com.techlambdas.delearmanagementapp.response;

import com.techlambdas.delearmanagementapp.constant.PaymentType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingResponse {
    private String bookingNo;
    private LocalDate bookingDate;
    private String customerId;
    private String customerName;
    private String mobileNo;
    private String address;
    private String partNo;
    private String categoryId;
    private String categoryName;
    private String itemName;
    private String additionalInfo;
    private PaymentType paymentType;
    private double amount;
    private boolean isCancelled;
}
