package com.techlambdas.delearmanagementapp.response;

import lombok.Data;

@Data
public class GstDetailResponse {
    private String gstName;
    private double percentage;
    private double gstAmount;
}
