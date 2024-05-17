package com.techlambdas.delearmanagementapp.service;


import com.techlambdas.delearmanagementapp.exception.AlreadyExistException;
import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.EmployeeMapper;
import com.techlambdas.delearmanagementapp.model.Employee;
import com.techlambdas.delearmanagementapp.repository.EmployeeCustomRepository;
import com.techlambdas.delearmanagementapp.repository.EmployeeRepository;
import com.techlambdas.delearmanagementapp.request.EmployeeReq;
import com.techlambdas.delearmanagementapp.response.EmployeeResponse;
import com.techlambdas.delearmanagementapp.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeCustomRepository employeeCustomRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

//    @Value("${app.image.upload-dir:./employeeImages}")
//    private String uploadFolder;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Override
    public Employee createEmployee(EmployeeReq employeeReq) {
        Employee existingEmployee=employeeRepository.findEmployeeByMobileNumber(employeeReq.getMobileNumber());
        if (existingEmployee!=null)
            throw new AlreadyExistException("MobileNumber Already Exist:"+existingEmployee.getMobileNumber());
        Employee employee= employeeMapper.mapEmployeeReqToEntity(employeeReq);
        String generatedId= RandomIdGenerator.getRandomId();
        LocalDate currentDate = LocalDate.now();
        LocalDate dateOfBirth = currentDate.minusYears(employeeReq.getAge());
        employee.setDateOfBirth(dateOfBirth);
        employee.setEmployeeId(generatedId);
        return employeeRepository.save(employee);
    }

    @Override
    public Page<EmployeeResponse> getEmployeesByPagination(String employeeName, String mobileNumber, String designation,String branchId,String branchName, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize);
        Page<Employee> employeeList = employeeCustomRepository.getAllEmployeesWithPage(employeeName,mobileNumber,designation,branchId,branchName,pageable);
        List<EmployeeResponse>employeeResponses=employeeMapper.mapEmployeeResponseListWithEmployees(employeeList.getContent());
       for (EmployeeResponse employeeResponse:employeeResponses){
           if (employeeResponse.getDateOfBirth()!=null)
           updateAgeInEmployeeResponse(employeeResponse);
       }
        return new PageImpl<>(employeeResponses, pageable, employeeList.getTotalElements());
    }
    @Override
    public List<EmployeeResponse> getAllEmployees(String employeeName, String mobileNumber) {
        List<Employee> employees= findAllEmployees(employeeName,mobileNumber);
        List<EmployeeResponse>employeeResponses=employeeMapper.mapEmployeeResponseListWithEmployees(employees);
        for (EmployeeResponse employeeResponse:employeeResponses){
            if (employeeResponse.getDateOfBirth()!=null)
                updateAgeInEmployeeResponse(employeeResponse);
        }
        return employeeResponses;

    }

    @Override
    public EmployeeResponse getEmployeeById(String employeeId) {
        Employee employee=employeeRepository.findByEmployeeId(employeeId);
        if (employee==null)
            throw new DataNotFoundException("Employee Not found this employeeId:"+employeeId);
        EmployeeResponse employeeResponse=employeeMapper.mapEmployeeResponseWithEntity(employee);
            if (employeeResponse.getDateOfBirth()!=null)
                updateAgeInEmployeeResponse(employeeResponse);
        return employeeResponse;
    }

    @Override
    public EmployeeResponse getEmployeeByMobileNo(String mobileNumber) {
        Employee employee=employeeRepository.findEmployeeByMobileNumber(mobileNumber);
        if (employee==null)
            throw new DataNotFoundException("Employee Not found this no:"+mobileNumber);
        EmployeeResponse employeeResponse=employeeMapper.mapEmployeeResponseWithEntity(employee);
            if (employeeResponse.getDateOfBirth()!=null)
                updateAgeInEmployeeResponse(employeeResponse);
        return employeeResponse;
    }

    @Override
    public EmployeeResponse updateEmployee(String employeeId, EmployeeReq employeeReq) {
        Employee employee=employeeRepository.findByEmployeeId(employeeId);
        if (employee==null)
            throw new DataNotFoundException("Employee Not found this employeeId:"+employeeId);
         employeeMapper.updateEmployeeToEmployeeReq(employeeReq,employee);
        LocalDate currentDate = LocalDate.now();
        LocalDate dateOfBirth = currentDate.minusYears(employeeReq.getAge());
        employee.setDateOfBirth(dateOfBirth);
        employeeRepository.save(employee);
        EmployeeResponse employeeResponse=employeeMapper.mapEmployeeResponseWithEntity(employee);
        if (employeeResponse.getDateOfBirth()!=null)
            updateAgeInEmployeeResponse(employeeResponse);
        return employeeResponse;
    }

    @Override
    public String deleteEmployeeById(String employeeId) {
        Employee employee=employeeRepository.findByEmployeeId(employeeId);
        if (employee==null)
            throw new DataNotFoundException("Employee Not found this employeeId:"+employeeId);
            employeeRepository.delete(employee);
        return "Deleted SuccessFully";
    }




    private void updateAgeInEmployeeResponse(EmployeeResponse employeeResponse) {
        LocalDate currentDate = LocalDate.now();
        int age= Period.between(employeeResponse.getDateOfBirth(), currentDate).getYears();
        employeeResponse.setAge(age);
    }

    private List<Employee> findAllEmployees(String employeeName, String mobileNumber) {
        Query query = new Query();
        if (employeeName != null) {
            Criteria criteria = new Criteria();
            Pattern regex = Pattern.compile("^" + employeeName, Pattern.CASE_INSENSITIVE);
            criteria = Criteria.where("employeeName").regex(regex);
            query.addCriteria(criteria);
        }
        if (mobileNumber!=null)
        {
                query.addCriteria(Criteria.where("mobileNumber").regex("^.*" + mobileNumber + ".*", "i"));
        }
        query.with(Sort.by(Sort.Direction.DESC, "createdDateTime"));
        return mongoTemplate.find(query, Employee.class);
    }
}
