package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeCustomRepository {
    Page<Employee> getAllEmployeesWithPage(String employeeName, String mobileNumber, String designation, String branchId,String branchName,Pageable pageable);

}
