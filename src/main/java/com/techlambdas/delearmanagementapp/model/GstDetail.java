package com.techlambdas.delearmanagementapp.model;

import lombok.Data;

@Data
public class GstDetail {
    private GstType gstName;
    private double percentage;
    private double gstAmount;
}
