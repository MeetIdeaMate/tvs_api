package com.techlambdas.delearmanagementapp.model;

import com.techlambdas.delearmanagementapp.config.AccessLevel;
import lombok.Data;

import java.util.List;

@Data
public class UiComponent {
    private String componentName;
    private List<AccessLevel> accessLevels;
}
