package com.techlambdas.delearmanagementapp.controller;
import com.techlambdas.delearmanagementapp.model.Employee;
import com.techlambdas.delearmanagementapp.request.EmployeeReq;
import com.techlambdas.delearmanagementapp.response.EmployeeResponse;
import com.techlambdas.delearmanagementapp.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.techlambdas.delearmanagementapp.response.AppResponse.successResponse;


@RestController
@RequestMapping(path = "/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeReq employeeReq) {
        Employee employee= employeeService.createEmployee(employeeReq);
        return successResponse(HttpStatus.CREATED,"employee",employee);
    }
    @GetMapping("/getByPagination")
    public ResponseEntity<Page<EmployeeResponse>> getEmployeesByPagination(@RequestParam(value = "employeeName", required = false) String employeeName,
                                                                           @RequestParam(value = "mobileNumber", required = false) String mobileNumber,
                                                                           @RequestParam(value = "designation", required = false) String designation ,
                                                                           @RequestParam(value = "branchId",required = false)String branchId,
                                                                           @RequestParam(value = "branch",required = false)String branchName,
                                                                           @RequestParam(value = "page", defaultValue = "0") int page,
                                                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page<EmployeeResponse> employees = employeeService. getEmployeesByPagination(employeeName,mobileNumber,designation,branchId,branchName,page,pageSize);
        return successResponse(HttpStatus.OK,"employeesWithPage",employees);
    }
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees(@RequestParam(value = "employeeName", required = false) String employeeName,

                                                          @RequestParam(value = "mobileNo", required = false) String mobileNumber) {
       List<EmployeeResponse> employees= employeeService.getAllEmployees(employeeName, mobileNumber);
        return successResponse(HttpStatus.OK,"employeeList",employees);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable String employeeId) {
        EmployeeResponse employee= employeeService.getEmployeeById(employeeId);
        return successResponse(HttpStatus.OK,"employee",employee);
    }
    @GetMapping("/getByMobile/{mobileNumber}")
    public ResponseEntity<EmployeeResponse> getEmployeeByMobileNo(@PathVariable String mobileNumber) {
       EmployeeResponse employee=employeeService.getEmployeeByMobileNo(mobileNumber);
        return successResponse(HttpStatus.OK,"employee",employee);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable String employeeId, @RequestBody EmployeeReq employeeReq) {
     EmployeeResponse employee=employeeService.updateEmployee(employeeId, employeeReq);
        return successResponse(HttpStatus.OK,"employee",employee);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String employeeId) {
       String result= employeeService.deleteEmployeeById(employeeId);
        return successResponse(HttpStatus.OK,"success",result);
    }


}
