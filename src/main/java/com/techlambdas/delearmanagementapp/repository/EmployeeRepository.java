package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee,String> {
    Employee findByEmployeeId(String employeeId);


    Employee findEmployeeByMobileNumber(String mobileNumber);

    default String getEmployeeName(String employeeId)
    {
        Employee employee = findByEmployeeId(employeeId);
        return (employee !=null)?employee.getEmployeeName():"unknown Employee";
    }
}
