package com.techlambdas.delearmanagementapp.response;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeResponse {
    private String id;
    private String employeeId;
    private String employeeName;
    private String gender;
    private String designation;
    private String branchId;
    private String branchName;
    private String emailId;
    private String mobileNumber;
    private int age;
    private LocalDate dateOfBirth;
    private String city;
    private String address;
}
