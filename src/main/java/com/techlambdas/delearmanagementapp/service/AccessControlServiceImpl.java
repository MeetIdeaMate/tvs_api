package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.AccessControlMapper;
import com.techlambdas.delearmanagementapp.model.AccessControl;
import com.techlambdas.delearmanagementapp.repository.AccessControlRepository;
import com.techlambdas.delearmanagementapp.request.AccessControlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessControlServiceImpl implements AccessControlService {

    @Autowired
    private AccessControlRepository accessControlRepository;

    @Autowired
    private AccessControlMapper accessControlMapper;

    @Override
    public AccessControl createAccessControl(AccessControlRequest request) {
        AccessControl accessControl = accessControlMapper.toEntity(request);
        return accessControlRepository.save(accessControl);
    }

    @Override
    public AccessControl updateAccessControl(String id, AccessControlRequest request) {
            AccessControl existingAccessControl =  accessControlRepository.findById(id).get();
            if (existingAccessControl!=null){
                accessControlMapper.updateAccessControlFromRequest(request,existingAccessControl);
            return accessControlRepository.save(existingAccessControl);
        } else {
            throw new DataNotFoundException("AccessControl not found");
        }
    }

    @Override
    public AccessControl getAccessControlById(String id) {
        AccessControl existingAccessControl =  accessControlRepository.findById(id).get();
        if (existingAccessControl!=null){
            return existingAccessControl;
        } else {
                throw new DataNotFoundException("AccessControl not found");
            }
    }

    @Override
    public List<AccessControl> getAllAccessControls() {
        return accessControlRepository.findAll();
    }
}
