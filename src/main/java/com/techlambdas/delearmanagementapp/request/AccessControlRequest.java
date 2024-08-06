package com.techlambdas.delearmanagementapp.request;

import com.techlambdas.delearmanagementapp.model.Menu;

import com.techlambdas.delearmanagementapp.model.UiComponent;
import lombok.Data;

import java.util.List;

@Data

public class AccessControlRequest {
    private String departmentId;
    private String branchId;
    private String userId;
    private String role;
    private String designation;
    private List<Menu> menus;
    private List<UiComponent>uiComponents;
}