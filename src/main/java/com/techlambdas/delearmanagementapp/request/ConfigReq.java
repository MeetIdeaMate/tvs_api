package com.techlambdas.delearmanagementapp.request;

import lombok.Data;

import java.util.List;

@Data
public class ConfigReq {
    private String configId;
    private List<String> configuration;
    private String defaultValue;

}
