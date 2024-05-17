package com.techlambdas.delearmanagementapp.model;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@Document(collection = "employees")
public class Employee {
    @Id
    private String id;
    private String employeeId;
    private String employeeName;
    private String gender;
    private String designation;
    private String branchId;
    private String emailId;
    private String mobileNumber;
    private LocalDate dateOfBirth;
    private String city;
    private String address;
    @CreatedDate
    private LocalDateTime createdDateTime;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedDateTime;
    @LastModifiedBy
    private String updatedBy;
}