package com.techlambdas.delearmanagementapp.request;

import lombok.Data;

import java.util.List;

@Data
public class AccessAccoundHeadReq {
    private String operatorCode;
    private List<String> accessAccoundHeads;
}
