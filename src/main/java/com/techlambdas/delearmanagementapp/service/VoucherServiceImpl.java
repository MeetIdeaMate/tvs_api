package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.VoucherMapper;
import com.techlambdas.delearmanagementapp.model.Voucher;
import com.techlambdas.delearmanagementapp.repository.CustomVoucherRepository;
import com.techlambdas.delearmanagementapp.repository.VoucherRepository;
import com.techlambdas.delearmanagementapp.request.VoucherRequest;
import com.techlambdas.delearmanagementapp.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherServiceImpl implements VoucherService{
    @Autowired
    private VoucherMapper voucherMapper;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private CustomVoucherRepository customVoucherRepository;

    @Override
    public Voucher createVoucher(VoucherRequest voucherRequest) {
        try {
            Voucher voucher = voucherMapper.mapVoucherRequestToVoucher(voucherRequest);
            voucher.setVoucherId(RandomIdGenerator.getRandomId());
            return voucherRepository.save(voucher);
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public List<Voucher> getAllVouchers(String voucherId, LocalDate fromDate, LocalDate toDate) {
        List<Voucher> vouchers=customVoucherRepository.getAllVouchers(voucherId,fromDate,toDate);
        return vouchers;
    }

    @Override
    public Voucher updateVoucher(String voucherId, VoucherRequest voucherRequest) {
        try {
            Voucher existingVoucher = voucherRepository.findByVoucherId(voucherId);
            if (!Optional.ofNullable(existingVoucher).isPresent())
                throw new DataNotFoundException("Voucher not found with this ID: "+voucherId);
            voucherMapper.updateVoucherFromRequest(voucherRequest , existingVoucher);
            return voucherRepository.save(existingVoucher);
        }catch (DataNotFoundException ex){
            throw new DataNotFoundException("Data not found --"+ex.getMessage());
        }catch (Exception ex)
        {
            throw new RuntimeException("Internal Server Error -- "+ex.getMessage(),ex.getCause());
        }
    }

    @Override
    public Page<Voucher> getAllVouchersWithPage(String voucherId, LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        return customVoucherRepository.getAllVouchersWithPage(voucherId,fromDate,toDate,pageable);
    }

    @Override
    public Voucher getVoucherByVoucherId(String voucherId) {
        Voucher voucher=voucherRepository.findByVoucherId(voucherId);
        if (voucher == null)
            throw new DataNotFoundException("Voucher not found with ID :"+voucherId);
        return voucher;
    }
}
