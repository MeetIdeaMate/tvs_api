package com.techlambdas.delearmanagementapp.repository;


import com.techlambdas.delearmanagementapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserCustomRepository {

     Page<User> getAllUsersWithPage(String userName, String mobileNumber, String designation, Pageable pageable);
}
