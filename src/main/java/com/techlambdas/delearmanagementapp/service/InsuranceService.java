package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.model.Insurance;
import com.techlambdas.delearmanagementapp.request.InsuranceRequest;
import com.techlambdas.delearmanagementapp.response.InsuranceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InsuranceService {

    Insurance createInsurance(InsuranceRequest insuranceRequest);

    Insurance updateInsurance(InsuranceRequest insuranceRequest, String insuranceId);

    InsuranceResponse getInsuranceById(String insuranceId);

    List<InsuranceResponse> getAllInsurance();

    Page<InsuranceResponse> getByPagination(String customerName, String mobileNo, String invoiceNo,String insuranceCompanyName, Pageable pageable);

    void deleteInsurance(String insuranceId);

}
