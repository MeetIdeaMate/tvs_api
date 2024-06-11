package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CustomVoucherRepository {
    List<Voucher> getAllVouchers(String voucherId, LocalDate fromDate, LocalDate toDate);

    Page<Voucher> getAllVouchersWithPage(String voucherId, LocalDate fromDate, LocalDate toDate, Pageable pageable);
}
