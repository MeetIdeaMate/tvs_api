package com.techlambdas.delearmanagementapp.service;
import com.techlambdas.hospitalmanagement.constant.UserStatus;
import com.techlambdas.hospitalmanagement.model.User;
import com.techlambdas.hospitalmanagement.request.LoginReq;
import com.techlambdas.hospitalmanagement.request.UserPwdChangReq;
import com.techlambdas.hospitalmanagement.request.UserReq;
import com.techlambdas.hospitalmanagement.request.UserUpdateReq;
import com.techlambdas.hospitalmanagement.response.LoginResponse;
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
