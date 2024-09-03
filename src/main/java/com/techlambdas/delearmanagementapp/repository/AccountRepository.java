package com.techlambdas.delearmanagementapp.repository;


import com.techlambdas.delearmanagementapp.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends MongoRepository<Account,String> {
    Account findByTransacId(String transacId);


}
