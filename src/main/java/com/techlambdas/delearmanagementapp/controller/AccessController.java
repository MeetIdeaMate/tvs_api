package com.techlambdas.delearmanagementapp.controller;

import com.techlambdas.delearmanagementapp.model.AccessControl;
import com.techlambdas.delearmanagementapp.request.AccessControlRequest;
import com.techlambdas.delearmanagementapp.service.AccessControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/access-controls")
public class AccessController {

    @Autowired
    private AccessControlService accessControlService;

    @PostMapping
    public ResponseEntity<AccessControl> createAccessControl(@RequestBody AccessControlRequest accessControlRequest) {
        AccessControl accessControl = accessControlService.createAccessControl(accessControlRequest);
        return ResponseEntity.ok(accessControl);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccessControl> updateAccessControl(@PathVariable String id,@RequestBody AccessControlRequest accessControlRequest) {
        AccessControl accessControl = accessControlService.updateAccessControl(id, accessControlRequest);
        return ResponseEntity.ok(accessControl);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccessControl> getAccessControlById(@PathVariable String id) {
        AccessControl accessControl = accessControlService.getAccessControlById(id);
        return ResponseEntity.ok(accessControl);
    }

    @GetMapping
    public ResponseEntity<List<AccessControl>> getAllAccessControls() {
        List<AccessControl> accessControls = accessControlService.getAllAccessControls();
        return ResponseEntity.ok(accessControls);
    }
}
