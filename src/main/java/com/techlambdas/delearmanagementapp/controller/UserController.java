package com.techlambdas.delearmanagementapp.controller;

import com.techlambdas.delearmanagementapp.constant.UserStatus;
import com.techlambdas.delearmanagementapp.model.User;
import com.techlambdas.delearmanagementapp.request.LoginReq;
import com.techlambdas.delearmanagementapp.request.UserPwdChangReq;
import com.techlambdas.delearmanagementapp.request.UserReq;
import com.techlambdas.delearmanagementapp.request.UserUpdateReq;
import com.techlambdas.delearmanagementapp.response.LoginResponse;
import com.techlambdas.delearmanagementapp.response.UserResponse;
import com.techlambdas.delearmanagementapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.techlambdas.delearmanagementapp.response.AppResponse.successResponse;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @PutMapping("/changePwd")
    public ResponseEntity<String> changePassword(@RequestBody UserPwdChangReq userPwdChangReq){
        userService.changePassword(userPwdChangReq);
        return successResponse(HttpStatus.OK,"success","changedSuccessFully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginReq loginReq){
       LoginResponse loginResponse= userService.authenticateUser(loginReq);
        return successResponse(HttpStatus.OK,"login",loginResponse);
    }
    @PostMapping
    public ResponseEntity<UserResponse>createUser(@RequestBody UserReq userReq){
        User user=userService.createUser(userReq);
        return successResponse(HttpStatus.CREATED,"user",user);
    }
    @GetMapping("/{mobileNo}")
    public ResponseEntity<User>getUserByMobileNo(@PathVariable String mobileNo){
        User user=userService.getUserByMobileNo(mobileNo);
        return successResponse(HttpStatus.OK,"user",user);
    }
    @GetMapping("/getByPagination")
    public ResponseEntity<Page<UserResponse>> getUsersByPagination(@RequestParam(value = "userName", required = false) String userName,
                                                                   @RequestParam (value = "mobileNumber", required = false) String mobileNumber,
                                                                   @RequestParam (value = "designation", required = false ) String designation,
                                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        Page<UserResponse> users = userService.getUsersByPagination(userName,mobileNumber,designation,page,pageSize);
        return successResponse(HttpStatus.OK,"userWithPage",users);
    }
    @GetMapping
    public ResponseEntity<List<UserResponse >>getAllUser(){
       List<UserResponse> users=userService.getAllUser();
        return successResponse(HttpStatus.OK,"userList",users);
    }
    @PutMapping
    public ResponseEntity<User>updateUser(@RequestBody UserUpdateReq userUpdateReq){
        User user=userService.updateUser(userUpdateReq);
        return successResponse(HttpStatus.OK,"user",user);
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> changePassword(@PathVariable String userId){
        userService.deleteUser(userId);
        return successResponse(HttpStatus.OK,"success","DeletedSuccessFully");
    }
    @PatchMapping("/status/{userId}")
    public ResponseEntity<String>changeUserStatus(@PathVariable String userId,@RequestParam UserStatus status)
    {
        String resp=userService.changeUserStatus(userId,status);
        return successResponse(HttpStatus.OK,"success",resp);
    }
    @PatchMapping("/passwordReset/{userId}")
    public ResponseEntity<String>resetPassword(@PathVariable String userId)
    {
        String response=userService.resetPassword(userId);
        return successResponse(HttpStatus.OK,"success","changedSuccessFully");
    }
}
