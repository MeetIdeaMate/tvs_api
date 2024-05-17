package com.techlambdas.delearmanagementapp.request;
import lombok.Data;

import java.time.LocalDate;


@Data
public class EmployeeReq {
    private String employeeName;
    private String gender;
    private String designation;
    private String branchId;
    private String emailId;
    private String mobileNumber;
    private  int age;
    private String city;
    private String address;
}
