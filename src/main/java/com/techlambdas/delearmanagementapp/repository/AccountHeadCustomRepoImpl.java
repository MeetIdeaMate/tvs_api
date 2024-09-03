package com.techlambdas.delearmanagementapp.repository;


import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.constant.PricingFormat;
import com.techlambdas.delearmanagementapp.model.AccountHead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountHeadCustomRepoImpl implements AccountHeadCustomRepo{

    private final MongoTemplate mongoTemplate;

    @Autowired
    public AccountHeadCustomRepoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<AccountHead> searchAccountHead(int page, int size, String accountHeadCode, String accountHeadName, PricingFormat pricingFormat, Boolean isCashierOps, boolean activeStatus, AccountType accountType, String transferFrom) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"createdDateTime"));
        Query query = new Query();
        List<Criteria> criteria = new ArrayList<>();
        if(accountHeadCode != null && !accountHeadCode.isEmpty())
            criteria.add(Criteria.where("accountHeadCode").regex(accountHeadCode));
        if(accountHeadName != null && !accountHeadName.isEmpty())
            criteria.add(Criteria.where("accountHeadName").regex(accountHeadName));
        if(pricingFormat != null)
            criteria.add(Criteria.where("pricingFormat").is(pricingFormat));
        if(accountType != null ) {
            if(accountType== AccountType.CREDIT)
                criteria.add(Criteria.where("accountType").is(AccountType.CREDIT));
            else if(accountType==AccountType.DEBIT)
                criteria.add(Criteria.where("accountType").is(AccountType.DEBIT));
            else
            criteria.add(Criteria.where("accountType").is(accountType));
            }

        if(transferFrom != null){
            if (transferFrom.equals("Member"))
                criteria.add(Criteria.where("transferFrom").is("Member"));
            else if(transferFrom.equals("Non-Member"))
                criteria.add(Criteria.where("transferFrom").is("Non-Member"));
            else if(transferFrom.equals("Operator"))
                criteria.add(Criteria.where("transferFrom").is("Operator"));
            else
                criteria.add(Criteria.where("transerFrom").is(transferFrom));
        }
        if(isCashierOps != null)
            criteria.add(Criteria.where("isCashierOps").is(isCashierOps));

//       criteria.add(Criteria.where("activeStatus").is(activeStatus));
        System.out.println(criteria);

        Criteria cmncriteria = new Criteria();
        if(!criteria.isEmpty()){
            cmncriteria.orOperator(criteria.toArray(criteria.toArray(new Criteria[criteria.size()])));
        }
        cmncriteria.andOperator(Criteria.where("activeStatus").is(activeStatus));
        System.out.println(cmncriteria);
        query.addCriteria(cmncriteria);
        query.with(pageable);
        long count = mongoTemplate.count(new Query().addCriteria(cmncriteria), AccountHead.class);
        List<AccountHead> accHeadReaponse = mongoTemplate.find(query,AccountHead.class);
        return new PageImpl<AccountHead>(accHeadReaponse, pageable, count);

//        if(!criteria.isEmpty())
//            query.addCriteria(new Criteria().andOperator(
//                    criteria.toArray(criteria.toArray(new Criteria[criteria.size()]))
//            ));
//        List<AccountHead> accHeadResponse = mongoTemplate.find(query.with(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"createdDateTime"))),AccountHead.class);
//        return new PageImpl<AccountHead>(accHeadResponse, PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"createdDateTime")),accHeadResponse.size());
//
   }

    @Override
    public List<AccountHead> getAccessAccountHeads() {
        Query query = new Query();
        query.addCriteria(Criteria.where("cashierOps").is(true));
        query.addCriteria(Criteria.where("activeStatus").is(true));
        List<AccountHead> accHeadResponse = mongoTemplate.find(query,AccountHead.class);
        return accHeadResponse;
    }


}
