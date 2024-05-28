package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Vendor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VendorRepository extends MongoRepository<Vendor, String> {
    Vendor findByVendorId(String vendorId);
}
