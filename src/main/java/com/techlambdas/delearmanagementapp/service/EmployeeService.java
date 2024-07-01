package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.model.Employee;
import com.techlambdas.delearmanagementapp.request.EmployeeReq;
import com.techlambdas.delearmanagementapp.response.EmployeeResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {
    Employee createEmployee(EmployeeReq employeeReq);

    Page<EmployeeResponse> getEmployeesByPagination(String employeeName, String mobileNumber, String designation,String branchId,String branchName, int page, int pageSize);

    List<EmployeeResponse> getAllEmployees(String employeeName, String mobileNumber,String branchId,String branchName);

    EmployeeResponse getEmployeeById(String employeeId);

    EmployeeResponse getEmployeeByMobileNo(String mobileNumber);

    EmployeeResponse updateEmployee(String employeeId, EmployeeReq employeeReq);

    String deleteEmployeeById(String employeeId);



}
