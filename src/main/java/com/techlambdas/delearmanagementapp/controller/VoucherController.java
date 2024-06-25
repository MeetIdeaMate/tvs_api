package com.techlambdas.delearmanagementapp.controller;

import com.techlambdas.delearmanagementapp.model.Voucher;
import com.techlambdas.delearmanagementapp.request.VoucherRequest;
import com.techlambdas.delearmanagementapp.service.VoucherService;
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
@RequestMapping("/voucher")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @PostMapping
    public ResponseEntity<Voucher> createVoucher(@RequestBody VoucherRequest voucherRequest)
    {
        Voucher voucher=voucherService.createVoucher(voucherRequest);
        return successResponse(HttpStatus.CREATED,"voucher",voucher);
    }

    @GetMapping
    public ResponseEntity<List<Voucher>>getAllVouchers(@RequestParam(required = false) String voucherId,
                                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                       @RequestParam(required = false)  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate){
        List<Voucher> vouchers=voucherService.getAllVouchers(voucherId,fromDate,toDate);
        return successResponse(HttpStatus.OK,"vouchers",vouchers);
    }
    @PutMapping("/{voucherId}")
    public ResponseEntity<Voucher> updateVoucher(@PathVariable String voucherId,@RequestBody VoucherRequest voucherRequest){
        Voucher voucher=voucherService.updateVoucher(voucherId,voucherRequest);
        return successResponse(HttpStatus.OK,"VoucherWithPage",voucher);
    }
    @GetMapping("/page")
    public ResponseEntity<Page<Voucher>> getAllVoucherWithPage(
            @RequestParam(required = false) String voucherId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false)  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Voucher> vouchersPage = voucherService.getAllVouchersWithPage(voucherId,fromDate,toDate, pageable);
        return successResponse(HttpStatus.OK,"vouchersWithPage",vouchersPage);
    }
    @GetMapping("/{voucherId}")
    public ResponseEntity<Voucher> getVoucherById(@PathVariable String voucherId){
        Voucher voucher=voucherService.getVoucherByVoucherId(voucherId);
        return successResponse(HttpStatus.OK,"voucher",voucher);
    }
}
