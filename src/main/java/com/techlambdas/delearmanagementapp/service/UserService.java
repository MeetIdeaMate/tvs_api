package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.constant.UserStatus;
import com.techlambdas.delearmanagementapp.model.User;
import com.techlambdas.delearmanagementapp.request.LoginReq;
import com.techlambdas.delearmanagementapp.request.UserPwdChangReq;
import com.techlambdas.delearmanagementapp.request.UserReq;
import com.techlambdas.delearmanagementapp.request.UserUpdateReq;
import com.techlambdas.delearmanagementapp.response.LoginResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    String changePassword(UserPwdChangReq userPwdChangReq);

    LoginResponse authenticateUser(LoginReq loginReq);

    User createUser(UserReq userReq);

    User updateUser(UserUpdateReq userUpdateReq);

    User getUserByMobileNo(String mobileNo);

    List<User> getAllUser();

    String getUserNameByUserId(String createdBy);

    Page<User> getUsersByPagination(String userName, String mobileNumber, String designation, int page, int pageSize);

    void deleteUser(String userId);

    String changeUserStatus(String userId, UserStatus status);

    String resetPassword(String userId);
}
