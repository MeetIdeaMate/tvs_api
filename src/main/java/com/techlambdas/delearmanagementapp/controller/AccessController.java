package com.techlambdas.delearmanagementapp.controller;

import com.techlambdas.delearmanagementapp.model.AccessControl;
import com.techlambdas.delearmanagementapp.request.AccessControlRequest;
import com.techlambdas.delearmanagementapp.service.AccessControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.techlambdas.delearmanagementapp.response.AppResponse.successResponse;

@RestController
@RequestMapping("/access-controls")
public class AccessController {

    @Autowired
    private AccessControlService accessControlService;

    @PostMapping
    public ResponseEntity createAccessControl(@RequestBody AccessControlRequest accessControlRequest) {
        AccessControl accessControl = accessControlService.createAccessControl(accessControlRequest);
        return successResponse(HttpStatus.CREATED,"accessControl",accessControl);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccessControl(@PathVariable String id,@RequestBody AccessControlRequest accessControlRequest) {
        AccessControl accessControl = accessControlService.updateAccessControl(id, accessControlRequest);
        return successResponse(HttpStatus.OK,"accessControl",accessControl);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccessControl> getAccessControlById(@PathVariable String id) {
        AccessControl accessControl = accessControlService.getAccessControlById(id);
        return successResponse(HttpStatus.OK,"accessControl",accessControl);
    }

    @GetMapping
    public ResponseEntity<List<AccessControl>> getAllAccessControls(@RequestParam(required = false)String userId,
                                                                    @RequestParam(required = false)String departmentId,
                                                                    @RequestParam(required = false)String role,
                                                                    @RequestParam(required = false)String designation,
                                                                    @RequestParam(required = false)String branchId) {
        List<AccessControl> accessControls = accessControlService.getAllAccessControls(userId,departmentId,role,designation,branchId);
        return successResponse(HttpStatus.OK,"accessControlList",accessControls);
    }

}
