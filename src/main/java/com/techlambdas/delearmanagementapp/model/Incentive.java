package com.techlambdas.delearmanagementapp.model;

import lombok.Data;

@Data
public class Incentive {
    private String incentiveName;
    private double percentage;
    private double incentiveAmount;
}
