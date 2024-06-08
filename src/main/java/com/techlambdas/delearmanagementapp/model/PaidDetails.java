package com.techlambdas.delearmanagementapp.model;

import lombok.Data;

@Data
public class PaidDetails {
    private String id;
    private String paymentId;
    private String paymentDate;
    private String paidAmount;
    private PaymentType paymentType;
}
