package com.techlambdas.delearmanagementapp.request;

import com.techlambdas.delearmanagementapp.constant.PaymentType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PaidDetailReq {
    private LocalDate paymentDate;
    private double paidAmount;
    private PaymentType paymentType;
    private String paymentReference;
}
