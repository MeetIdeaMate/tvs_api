package com.techlambdas.delearmanagementapp.model;

import com.techlambdas.delearmanagementapp.constant.PaymentType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PaidDetail {
    private String paymentId;
    private LocalDate paymentDate;
    private double paidAmount;
    private PaymentType paymentType;
}
