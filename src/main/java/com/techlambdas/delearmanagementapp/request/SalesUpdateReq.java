package com.techlambdas.delearmanagementapp.request;

import com.techlambdas.delearmanagementapp.model.PaymentStatus;
import lombok.Data;

@Data
public class SalesUpdateReq {
    private String customerId;
    private PaymentStatus paymentStatus;
}
