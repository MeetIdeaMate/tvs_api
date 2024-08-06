package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.constant.BookingStatus;
import com.techlambdas.delearmanagementapp.constant.PaymentType;
import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.AccessControlMapper;
import com.techlambdas.delearmanagementapp.model.AccessControl;
import com.techlambdas.delearmanagementapp.model.Booking;
import com.techlambdas.delearmanagementapp.repository.AccessControlRepository;
import com.techlambdas.delearmanagementapp.request.AccessControlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccessControlServiceImpl implements AccessControlService {

    @Autowired
    private AccessControlRepository accessControlRepository;

    @Autowired
    private AccessControlMapper accessControlMapper;
    @Autowired
    private MongoTemplate mongoTemplate;

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
    public List<AccessControl> getAllAccessControls(String userId, String departmentId, String role, String designation, String branchId) {

        Query query = new Query();
        if (Optional.ofNullable(userId).isPresent()) {
            query.addCriteria(Criteria.where("userId").is(userId));

        }
        if (Optional.ofNullable(departmentId).isPresent()) {
            query.addCriteria(Criteria.where("departmentId").is(departmentId));

        }
        if (Optional.ofNullable(role).isPresent()) {
            query.addCriteria(Criteria.where("role").is(role));

        }
        if (Optional.ofNullable(designation).isPresent()) {
            query.addCriteria(Criteria.where("designation").is(designation));

        }
        if (Optional.ofNullable(branchId).isPresent()) {
            query.addCriteria(Criteria.where("branchId").is(branchId));
        }
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));
        return mongoTemplate.find(query, AccessControl.class);
    }
}
