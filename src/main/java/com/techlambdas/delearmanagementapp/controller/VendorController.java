//package com.techlambdas.delearmanagementapp.controller;
//import com.techlambdas.delearmanagementapp.model.Vendor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/vendor")
//public class VendorController {
//
//    @Autowired
//    VendorService vendorService;
//    @PostMapping
//    public ResponseEntity<Vendor>createVendor(@RequestBody VendorPostDTO vendorPostDTO){
//        Vendor vendor=vendorService.createVendor(vendorPostDTO);
//                return new ResponseEntity<>(vendor, HttpStatus.CREATED);
//    }
//    @PutMapping("/{vendorId}")
//    public ResponseEntity<Vendor>updateVendor(@PathVariable String vendorId,@RequestBody VendorPostDTO vendorPostDTO){
//    Vendor vendor=vendorService.updateVendor(vendorId,vendorPostDTO);
//    return new ResponseEntity<>(vendor, HttpStatus.OK);
//}
//    @PutMapping("/vendorType/{vendorId}")
//    public ResponseEntity<Vendor>updateVendorType(@PathVariable String vendorId,@RequestParam VendorType vendorType,@RequestBody List<Object> vendorObjectList){
//        Vendor vendor=vendorService.updateVendorType(vendorId,vendorType,vendorObjectList);
//        return new ResponseEntity<>(vendor, HttpStatus.OK);
//    }
//    @PutMapping("/Labour/{vendorId}")
//    public ResponseEntity<Vendor>updateLabourVendor(@PathVariable String vendorId,@RequestBody List<Labour> labourList){
//        Vendor vendor=vendorService.updateLabourVendor(vendorId,labourList);
//        return new ResponseEntity<>(vendor, HttpStatus.OK);
//    }
//    @PutMapping("/Machinery/{vendorId}")
//    public ResponseEntity<Vendor>updateMachineryVendor(@PathVariable String vendorId,@RequestBody List<Machinery> machineryList){
//        Vendor vendor=vendorService.updateMachineryVendor(vendorId,machineryList);
//        return new ResponseEntity<>(vendor, HttpStatus.OK);
//    }
//    @PutMapping("/RunningVehicle/{vendorId}")
//    public ResponseEntity<Vendor>updateRunningVehicleVendor(@PathVariable String vendorId,@RequestBody List<RunningVehicle> runningVehicles){
//        Vendor vendor=vendorService.updateRunningVehicleVendor(vendorId,runningVehicles);
//        return new ResponseEntity<>(vendor, HttpStatus.OK);
//    }
//    @PutMapping("/LoadingVehicle/{vendorId}")
//    public ResponseEntity<Vendor>updateLoadingVehicleVendor(@PathVariable String vendorId,@RequestBody List<LoadingVehicle> loadingVehicles){
//        Vendor vendor=vendorService.updateLoadingVehicleVendor(vendorId,loadingVehicles);
//        return new ResponseEntity<>(vendor, HttpStatus.OK);
//    }
//    @GetMapping("/{vendorId}")
//    public ResponseEntity<Vendor>getVendorById(@PathVariable String vendorId){
//        Vendor vendor=vendorService.getVendorById(vendorId);
//        return new ResponseEntity<>(vendor, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{vendorId}")
//    public ResponseEntity<Vendor>deleteVendorById(@PathVariable String vendorId){
//        Vendor vendor=vendorService.deleteVendorById(vendorId);
//        return new ResponseEntity<>(vendor, HttpStatus.OK);
//    }
//    @GetMapping
//    public ResponseEntity<List<Vendor>>getAllVendor(@RequestParam(required = false) VendorType vendorType){
//        List<Vendor> vendor=vendorService.getAllVendor(vendorType);
//        return new ResponseEntity<>(vendor, HttpStatus.OK);
//    }
//
//
//}
