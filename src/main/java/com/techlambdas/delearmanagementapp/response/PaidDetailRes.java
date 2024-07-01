package com.techlambdas.delearmanagementapp.response;

import com.techlambdas.delearmanagementapp.constant.PaymentType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PaidDetailRes {
    private String paymentId;
    private LocalDate paymentDate;
    private double paidAmount;
    private PaymentType paymentType;
    private boolean isCancelled;
    private String reason;
}
