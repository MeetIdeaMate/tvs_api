package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.exception.AlreadyExistException;
import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.CommonMapper;
import com.techlambdas.delearmanagementapp.mapper.InsuranceMapper;
import com.techlambdas.delearmanagementapp.model.Insurance;
import com.techlambdas.delearmanagementapp.repository.InsuranceCustomRepository;
import com.techlambdas.delearmanagementapp.repository.InsuranceRepository;
import com.techlambdas.delearmanagementapp.request.InsuranceRequest;
import com.techlambdas.delearmanagementapp.response.InsuranceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.techlambdas.delearmanagementapp.utils.RandomIdGenerator.getRandomId;

@Service
public class InsuranceServiceImpl implements InsuranceService{
    @Autowired
    InsuranceMapper insuranceMapper;
    @Autowired
    InsuranceRepository insuranceRepository;
    @Autowired
    InsuranceCustomRepository insuranceCustomRepository;
    @Autowired
    CommonMapper commonMapper;


    @Override
    public Insurance createInsurance(InsuranceRequest insuranceRequest) {
        try{
//            Insurance exsistIns = insuranceRepository.findByInsuranceNo(insuranceRequest.getInsuranceNo());
//            if(exsistIns!=null)
//                throw new AlreadyExistException("Insurance No AlreadyExist With Insurance No:" + insuranceRequest.getInsuranceNo());
            Insurance insurance = insuranceMapper.mapReqToEntity(insuranceRequest);
       insurance.setInsuranceId(getRandomId());
       return insuranceRepository.save(insurance);}
        catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }

    }

    @Override
    public Insurance updateInsurance(InsuranceRequest insuranceRequest, String insuranceId) {
        try{
        Insurance exsistingInsurance = insuranceRepository.findByInsuranceId(insuranceId);
        if(exsistingInsurance==null)
            throw new DataNotFoundException("Insurance Not Found With Id: " + insuranceId);
      return insuranceMapper.updateReqToEntity(insuranceRequest,exsistingInsurance);
      }
        catch (DataNotFoundException ex) {
            throw new DataNotFoundException("Data Not Found --" + ex.getMessage());
        }
        catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }}

    @Override
    public InsuranceResponse getInsuranceById(String insuranceId) {
        try {
            Insurance insurance = insuranceRepository.findByInsuranceId(insuranceId);
            if (insurance == null)
                throw new DataNotFoundException("Insurance Not Found With Id: " + insuranceId);
            return commonMapper.mapInsuranceToInsuranceRes(insurance);
        }
        catch (DataNotFoundException ex) {
            throw new DataNotFoundException("Data Not Found --" + ex.getMessage());
        }
        catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public List<InsuranceResponse> getAllInsurance() {
        try{
       List<Insurance> insuranceList =  insuranceRepository.findAll();
       return insuranceList.stream()
               .map(commonMapper::mapInsuranceToInsuranceRes)
               .collect(Collectors.toList());

    }catch (Exception ex){
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }

    }
    @Override
    public Page<InsuranceResponse> getByPagination(String customerName, String mobileNo, String invoiceNo, String insuranceCompanyName, Pageable pageable) {
      try{
     Page<Insurance> insuranceList = insuranceCustomRepository.getAllInsuranceWithPagination(customerName,mobileNo,invoiceNo,insuranceCompanyName,pageable);
          List<InsuranceResponse> insuranceResponses = insuranceList.getContent().stream()
                  .map(commonMapper::mapInsuranceToInsuranceRes)
                  .collect(Collectors.toList());

   return new PageImpl<>(insuranceResponses,pageable,insuranceList.getTotalElements());

      }catch (Exception ex){
          throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
      }
    }

    @Override
    public void deleteInsurance(String insuranceId) {
        Insurance insurance = insuranceRepository.findByInsuranceId(insuranceId);
        if (insurance == null)
            throw new DataNotFoundException("Insurance Not Found With Id: " + insuranceId);
        insuranceRepository.delete(insurance);
    }
}
