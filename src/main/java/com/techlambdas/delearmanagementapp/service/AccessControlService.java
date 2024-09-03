package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.model.AccessControl;
import com.techlambdas.delearmanagementapp.request.AccessControlRequest;

import java.util.List;

public interface AccessControlService {
    AccessControl createAccessControl(AccessControlRequest request);
    AccessControl updateAccessControl(String id, AccessControlRequest request);
    AccessControl getAccessControlById(String id);
    List<AccessControl> getAllAccessControls(String userId,String departmentId,String role,String designation,String branchId);
}