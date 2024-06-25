package com.techlambdas.delearmanagementapp.request;

import lombok.Data;

import java.util.Map;

@Data
public class CategoryRequest {
    private String categoryName;
    private Map<Integer,String> mainSpec;
    private Map<Integer,String> specification;
    private Map<String,Integer> taxes;
    private Map<String,Integer> incentive;
}
