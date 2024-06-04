package com.techlambdas.delearmanagementapp.controller;


import com.techlambdas.delearmanagementapp.model.Customer;
import com.techlambdas.delearmanagementapp.request.CustomerRequest;
import com.techlambdas.delearmanagementapp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.techlambdas.delearmanagementapp.response.AppResponse.successResponse;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody CustomerRequest customerRequest){
        Customer customer=customerService.createCustomer(customerRequest);
        return new ResponseEntity<>(customer, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<Customer>>getAllCustomers(@RequestParam (required = false)String customerId,
                                                         @RequestParam (required = false)String customerName,
                                                         @RequestParam (required = false)String mobileNo,
                                                         @RequestParam (required = false)String city){
        List<Customer> customers=customerService.getAllCustomers(customerId,customerName,mobileNo,city);
        return successResponse(HttpStatus.OK,"customers",customers);
    }
    @GetMapping("/page")
    public ResponseEntity<Page<Customer>> getAllCustomersWithPage(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String mobileNo,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customersPage = customerService.getAllCustomersWithPage(customerId, customerName, mobileNo, city, pageable);

        return successResponse(HttpStatus.OK,"customersWithPage",customersPage);
    }
    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String customerId){
        Customer customer=customerService.getCustomerByCustomerId(customerId);
        return successResponse(HttpStatus.OK,"customer",customer);
    }


    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String customerId,@RequestBody CustomerRequest customerRequest){
        Customer customer=customerService.updateCustomer(customerId,customerRequest);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
    @DeleteMapping("/{customerId}")
    public ResponseEntity deleteCustomer(@PathVariable String customerId){
         customerService.deleteByCustomerId(customerId);
        return new ResponseEntity<>("Deleted SuccessFully",HttpStatus.OK);
    }
}
