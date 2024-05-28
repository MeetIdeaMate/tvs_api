package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.VendorMapper;
import com.techlambdas.delearmanagementapp.model.Vendor;
import com.techlambdas.delearmanagementapp.repository.CustomVendorRepository;
import com.techlambdas.delearmanagementapp.request.VendorRequest;
import com.techlambdas.delearmanagementapp.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.techlambdas.delearmanagementapp.repository.VendorRepository;

import java.util.List;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private VendorMapper vendorMapper;
    @Autowired
    private CustomVendorRepository  customVendorRepository;

    public Vendor createVendor(VendorRequest vendorRequest) {
        try {
            Vendor vendor = vendorMapper.mapVendorRequestToVendor(vendorRequest);
            vendor.setVendorId(RandomIdGenerator.getRandomId());
            return vendorRepository.save(vendor);
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }
    @Override
    public List<Vendor> getAllVendors(String vendorId, String vendorName, String mobileNo, String city) {
        List<Vendor> vendors=customVendorRepository.getAllVendors(vendorId,vendorName,mobileNo,city);
        return vendors;
    }
    @Override
    public Vendor updateVendor(String vendorId, VendorRequest vendorRequest) {
        try {
            Vendor existingVendor = vendorRepository.findByVendorId(vendorId);
            if (existingVendor == null)
                throw new DataNotFoundException("vendor not found with ID: " + vendorId);
            vendorMapper.updateVendorFromRequest(vendorRequest, existingVendor);
            return vendorRepository.save(existingVendor);
        }catch (DataNotFoundException ex) {
            throw new DataNotFoundException("Data not found --" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }
    @Override
    public Page<Vendor> getAllVendorsWithPage(String vendorId, String vendorName, String mobileNo, String city,  Pageable pageable) {
        return customVendorRepository.getAllVendorsWithPage(vendorId,vendorName,mobileNo,city,  pageable);
    }
}
