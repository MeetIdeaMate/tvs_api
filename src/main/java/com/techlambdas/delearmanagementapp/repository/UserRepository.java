package com.techlambdas.delearmanagementapp.repository;


import com.techlambdas.hospitalmanagement.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {

    User findUserByMobileNumber(String mobileNo);

    User findUserByUserId(String userId);
    Optional<User> findByUserName(String username);

}
