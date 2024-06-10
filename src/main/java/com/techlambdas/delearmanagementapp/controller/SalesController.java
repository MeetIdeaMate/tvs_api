package com.techlambdas.delearmanagementapp.controller;

import com.techlambdas.delearmanagementapp.model.Sales;
import com.techlambdas.delearmanagementapp.request.SalesRequest;
import com.techlambdas.delearmanagementapp.request.SalesUpdateReq;
import com.techlambdas.delearmanagementapp.response.SalesResponse;
import com.techlambdas.delearmanagementapp.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

//    @GetMapping
//    public ResponseEntity<List<Sales>> getAllSales() {
//        List<Sales> sales = salesService.getAllSales();
//        return successResponse(HttpStatus.OK, "salesList", sales);
//    }

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

//    @GetMapping
//    public ResponseEntity<List<SalesResponse>> getAllSalesView(@RequestParam(required = false) String invoiceNo) {
//        List<SalesResponse> sales = salesService.getAllSalesView(invoiceNo);
//        return successResponse(HttpStatus.OK, "sales", sales);
//    }


@GetMapping
public ResponseEntity<List<SalesResponse>>getAllSales(@RequestParam(required = false) String invoiceNo){
    List<SalesResponse> sales=salesService.getAllSales(invoiceNo);
    return successResponse(HttpStatus.OK,"sales",sales);
}


    @GetMapping("/page")
    public ResponseEntity<Page<SalesResponse>> getAllSalesWithPage(
            @RequestParam(required = false) String invoiceNo,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<SalesResponse> SalesPage = salesService.getAllSalesWithPage(invoiceNo,categoryName, fromDate,toDate,pageable);

        return successResponse(HttpStatus.OK,"salesWithPage",SalesPage);
    }


}
