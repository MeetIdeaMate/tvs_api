package com.techlambdas.delearmanagementapp.controller;

import com.techlambdas.delearmanagementapp.model.Sales;
import com.techlambdas.delearmanagementapp.model.User;
import com.techlambdas.delearmanagementapp.request.SalesRequest;
import com.techlambdas.delearmanagementapp.request.SalesUpdateReq;
import com.techlambdas.delearmanagementapp.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.techlambdas.delearmanagementapp.response.AppResponse.successResponse;

@RestController
@RequestMapping("/sales")
public class SalesController {

    @Autowired
    SalesService salesService;

    @PostMapping
    public ResponseEntity<Sales> createSales(@RequestBody SalesRequest salesRequest) {
        Sales sales = salesService.createSales(salesRequest);
        return successResponse(HttpStatus.CREATED, "sales", sales);
    }

    @GetMapping
    public ResponseEntity<List<Sales>> getAllSales() {
        List<Sales> sales = salesService.getAllSales();
        return successResponse(HttpStatus.OK, "salesList", sales);
    }

    @PutMapping
    public ResponseEntity<Sales> updateSales(@RequestBody SalesUpdateReq salesUpdateReq) {
        Sales sales = salesService.updateSales(salesUpdateReq);
        return successResponse(HttpStatus.OK, "sales", sales);
    }

    @DeleteMapping("/{invoiceNo}")
    public ResponseEntity<String> changePassword(@PathVariable String invoiceNo) {
        salesService.deleteSales(invoiceNo);
        return successResponse(HttpStatus.OK, "success", "DeletedSuccessFully");
    }

    @GetMapping("/{invoiceNo}")
    public ResponseEntity<Sales> getSalesByInvoiceNo(@PathVariable String invoiceNo){
        Sales sales =  salesService.getSalesByInvoiceNo(invoiceNo);
        return successResponse(HttpStatus.OK,"sales",sales);
    }


}
