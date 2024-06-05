package com.techlambdas.delearmanagementapp.request;

import com.techlambdas.delearmanagementapp.model.GstType;
import lombok.Data;

@Data
public class GstDetailRequest {
    private GstType gstName;
    private double percentage;
}
