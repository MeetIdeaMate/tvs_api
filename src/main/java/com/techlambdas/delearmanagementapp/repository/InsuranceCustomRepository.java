package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Insurance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InsuranceCustomRepository {

     Page<Insurance> getAllInsuranceWithPagination(String customerName, String mobileNo, String invoiceNo, String insuranceCompanyName, Pageable pageable);
}
