package com.techlambdas.delearmanagementapp.service;


import com.techlambdas.delearmanagementapp.model.Vendor;
import com.techlambdas.delearmanagementapp.request.VendorRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VendorService {
    Vendor createVendor(VendorRequest vendorRequest);
    List<Vendor> getAllVendors(String vendorId, String vendorName, String mobileNo, String city);
    Vendor updateVendor(String vendorId, VendorRequest vendorRequest);
    Page<Vendor> getAllVendorsWithPage(String vendorId, String vendorName, String mobileNo, String city, Pageable pageable);

    Vendor getVendorByVendorId(String vendorId);
}
