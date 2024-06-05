package com.techlambdas.delearmanagementapp.response;

import com.techlambdas.delearmanagementapp.model.Insurance;
import com.techlambdas.delearmanagementapp.model.ItemDetail;
import com.techlambdas.delearmanagementapp.model.PaymentStatus;
import com.techlambdas.delearmanagementapp.model.loanInfo;
import lombok.Data;

import java.util.List;

@Data
public class SalesResponse {
    private String customerId;
    private PaymentStatus paymentStatus;
    private List<ItemDetail> itemDetails;



}
