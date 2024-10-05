package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Insurance;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InsuranceRepository extends MongoRepository<Insurance,String> {

    Insurance findByInsuranceId(String insuranceId);

    Insurance findByInsuranceNo(String insuranceNo);

}
