package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.hospitalmanagement.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<User> getAllUsersWithPage(String userName, String mobileNumber, String designation,Pageable pageable) {
        Query query = new Query();
        if (userName!=null) {

            Criteria criteria = Criteria.where("userName").regex("^.*" + userName + ".*", "i");
            query.addCriteria(criteria);
        }
        if (mobileNumber!=null) {
            query.addCriteria(Criteria.where("mobileNumber").regex("^.*" + mobileNumber + ".*", "i"));
        }
        if (designation!=null){
            query.addCriteria(Criteria.where("designation").is( designation ));
        }
        query.with(Sort.by(Sort.Order.desc("createdDateTime")));
        long totalCount = mongoTemplate.count(query, User.class);
        query.with(pageable);
        List<User> userList = mongoTemplate.find(query, User.class);

        return new PageImpl<>(userList, pageable, totalCount);

    }
}
