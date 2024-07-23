package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.model.AccountHead;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AccountHeadRepository extends MongoRepository<AccountHead, String>,AccountHeadCustomRepo {
    boolean existsByAccountHeadCode(String accountHeadCode);

    AccountHead findByAccountHeadCode(String accountHeadCode);

    List<AccountHead> findByAccountType(AccountType accountType);
}
