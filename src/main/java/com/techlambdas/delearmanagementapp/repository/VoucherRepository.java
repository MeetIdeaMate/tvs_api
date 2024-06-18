package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Voucher;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoucherRepository extends MongoRepository<Voucher,String> {
    Voucher findByVoucherId(String voucherId);
}
