package com.techlambdas.delearmanagementapp.controller;


import com.techlambdas.delearmanagementapp.model.Insurance;
import com.techlambdas.delearmanagementapp.request.InsuranceRequest;
import com.techlambdas.delearmanagementapp.response.InsuranceResponse;
import com.techlambdas.delearmanagementapp.service.InsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.techlambdas.delearmanagementapp.response.AppResponse.successResponse;

@RestController
@RequestMapping("insurance")
public class InsuranceController {

    @Autowired
    InsuranceService insuranceService;

    @PostMapping
    public ResponseEntity createInsurance(@RequestBody InsuranceRequest insuranceRequest){
        Insurance insurance = insuranceService.createInsurance(insuranceRequest);
        return successResponse(HttpStatus.OK,"insurance",insurance);
    }
    @PutMapping("/{insuranceId}")
    public ResponseEntity updateInsurance(@RequestBody InsuranceRequest insuranceRequest,@PathVariable String insuranceId){
        Insurance insurance = insuranceService.updateInsurance(insuranceRequest,insuranceId);
        return successResponse(HttpStatus.OK,"insurance",insurance);
    }
    @GetMapping("/{insuranceId}")
    public ResponseEntity getInsuranceById(@PathVariable String insuranceId){
        InsuranceResponse insuranceResponse = insuranceService.getInsuranceById(insuranceId);
        return successResponse(HttpStatus.OK,"insurance",insuranceResponse);
    }
    @GetMapping
    public ResponseEntity getAllInsurance(){
        return successResponse(HttpStatus.OK,"insuranceList",insuranceService.getAllInsurance());
    }
    @GetMapping("/getAllInsuranceWithPagination")
    public ResponseEntity getAllInsuranceWithPagination(@RequestParam(required = false) String customerName,@RequestParam(required = false) String mobileNo,@RequestParam(required = false) String invoiceNo,@RequestParam(required = false) String insuranceCompanyName,@RequestParam( defaultValue = "0") int page,@RequestParam(required = false, defaultValue = "10") int pageSize)
    {
        Pageable pageable = PageRequest.of(page,pageSize);
        Page<InsuranceResponse> insuranceResponses = insuranceService.getByPagination(customerName,mobileNo,invoiceNo,insuranceCompanyName,pageable);
        return successResponse(HttpStatus.OK,"insuranceListWithPage",insuranceResponses);
    }



}
