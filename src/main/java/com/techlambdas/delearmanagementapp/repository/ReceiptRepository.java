package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Receipt;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReceiptRepository extends MongoRepository<Receipt,String> {
    Receipt findByReceiptId(String receiptId);
}
