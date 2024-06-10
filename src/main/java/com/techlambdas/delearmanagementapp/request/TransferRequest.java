package com.techlambdas.delearmanagementapp.request;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TransferRequest {
    private List<TransferItemReq>transferItems;
    private String transferFromBranch;
    private String transferToBranch;
}
