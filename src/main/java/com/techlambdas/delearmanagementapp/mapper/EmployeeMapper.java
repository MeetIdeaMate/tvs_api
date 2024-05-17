package com.techlambdas.delearmanagementapp.mapper;


import com.techlambdas.delearmanagementapp.model.Employee;
import com.techlambdas.delearmanagementapp.request.EmployeeReq;
import com.techlambdas.delearmanagementapp.response.EmployeeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")

public interface EmployeeMapper {
    Employee mapEmployeeReqToEntity(EmployeeReq employeeReq);
    

    List<EmployeeResponse> mapEmployeeResponseListWithEmployees(List<Employee> employees);

    EmployeeResponse mapEmployeeResponseWithEntity(Employee employee);

    void updateEmployeeToEmployeeReq(EmployeeReq employeeReq, @MappingTarget Employee employee);
}
