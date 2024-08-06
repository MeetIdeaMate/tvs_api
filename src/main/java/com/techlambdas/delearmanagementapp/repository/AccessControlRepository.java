package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.AccessControl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessControlRepository extends MongoRepository<AccessControl, String> {

    List<AccessControl> findByUserId(String userId);

    List<AccessControl> findByDepartmentId(String departmentId);

    List<AccessControl> findByRole(String role);

    List<AccessControl> findByDesignation(String designation);

    List<AccessControl> findByBranchId(String branchId);
}
