package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.model.Voucher;
import com.techlambdas.delearmanagementapp.request.VoucherRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface VoucherService {
    Voucher createVoucher(VoucherRequest voucherRequest);

    List<Voucher> getAllVouchers(String voucherId, LocalDate fromDate, LocalDate toDate);

    Voucher updateVoucher(String voucherId, VoucherRequest voucherRequest);

    Page<Voucher> getAllVouchersWithPage(String voucherId, LocalDate fromDate, LocalDate toDate, Pageable pageable);

    Voucher getVoucherByVoucherId(String voucherId);
}
