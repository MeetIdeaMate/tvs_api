package com.techlambdas.delearmanagementapp.response;

import lombok.Data;

@Data
public class IncentiveResponse {
    private String incentiveName;
    private double percentage;
    private double incentiveAmount;
}
