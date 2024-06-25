package com.techlambdas.delearmanagementapp.request;

import lombok.Data;

import java.util.Map;

@Data
public class TransferItemReq {
    private String stockId;
    private String partNo;
    private int quantity;
}