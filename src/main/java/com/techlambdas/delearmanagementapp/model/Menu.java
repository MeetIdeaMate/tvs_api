package com.techlambdas.delearmanagementapp.model;

import com.techlambdas.delearmanagementapp.config.AccessLevel;
import lombok.Data;

import java.util.List;

@Data
public class Menu {
    private String menuName;
    private List<AccessLevel>accessLevels;
}
