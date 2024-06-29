package com.techlambdas.delearmanagementapp.model;

import com.techlambdas.delearmanagementapp.constant.PaymentType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDate;

@Data
public class PaidDetail {
    private String paymentId;
    private LocalDate paymentDate;
    private double paidAmount;
    private PaymentType paymentType;
    private boolean isCancelled;
}
