package com.techlambdas.delearmanagementapp.request;

import lombok.Data;

import java.util.List;

@Data
public class StockAddReq {
    private List<String> partNos;
}
