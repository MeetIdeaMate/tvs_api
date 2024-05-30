package com.techlambdas.delearmanagementapp.controller;

import com.techlambdas.delearmanagementapp.model.Customer;
import com.techlambdas.delearmanagementapp.model.Vendor;
import com.techlambdas.delearmanagementapp.request.VendorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.techlambdas.delearmanagementapp.response.AppResponse.successResponse;
import com.techlambdas.delearmanagementapp.service.VendorService;

import java.util.List;

@RestController
@RequestMapping("/vendor")
public class VendorController {
    @Autowired
    private VendorService vendorService;
    @PostMapping
    public ResponseEntity<Vendor> createVendor(@RequestBody VendorRequest vendorRequest) {
        Vendor vendor = vendorService.createVendor(vendorRequest);
        return successResponse(HttpStatus.CREATED,"vendor",vendor);
    }
    @GetMapping
    public ResponseEntity<List<Vendor>>getAllVendors(@RequestParam(required = false) String vendorId,
                                                       @RequestParam(required = false) String vendorName,
                                                       @RequestParam(required = false) String mobileNo,
                                                       @RequestParam(required = false) String city){
        List<Vendor> vendors=vendorService.getAllVendors(vendorId,vendorName,mobileNo,city);
        return successResponse(HttpStatus.OK,"vendors",vendors);
    }
    @PutMapping("/{vendorId}")
    public ResponseEntity<Vendor> updateVendor(@PathVariable String vendorId,@RequestBody VendorRequest vendorRequest){
        Vendor vendor=vendorService.updateVendor(vendorId,vendorRequest);
        return successResponse(HttpStatus.OK,"VendorWithPage",vendor);
    }
    @GetMapping("/page")
    public ResponseEntity<Page<Vendor>> getAllVendorWithPage(
            @RequestParam(required = false) String vendorId,
            @RequestParam(required = false) String vendorName,
            @RequestParam(required = false) String mobileNo,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Vendor> vendorsPage = vendorService.getAllVendorsWithPage(vendorId, vendorName, mobileNo, city, pageable);
        return successResponse(HttpStatus.OK,"vendorsWithPage",vendorsPage);
    }
    @GetMapping("/{vendorId}")
    public ResponseEntity<Vendor> getVendorById(@PathVariable String vendorId){
        Vendor vendor=vendorService.getVendorByVendorId(vendorId);
        return successResponse(HttpStatus.OK,"vendor",vendor);
    }
}