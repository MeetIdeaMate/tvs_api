package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomVendorRepository {
    List<Vendor> getAllVendors(String vendorId, String vendorName, String mobileNo, String city);
    Page<Vendor> getAllVendorsWithPage(String vendorId, String vendorName, String mobileNo, String city, Pageable pageable);
}
