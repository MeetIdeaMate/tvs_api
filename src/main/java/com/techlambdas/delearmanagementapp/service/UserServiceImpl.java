package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.config.JwtUtils;
import com.techlambdas.delearmanagementapp.constant.UserStatus;
import com.techlambdas.delearmanagementapp.exception.AlreadyExistException;
import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.exception.InvalidDataException;
import com.techlambdas.delearmanagementapp.model.User;
import com.techlambdas.delearmanagementapp.repository.UserCustomRepository;
import com.techlambdas.delearmanagementapp.repository.UserRepository;
import com.techlambdas.delearmanagementapp.request.LoginReq;
import com.techlambdas.delearmanagementapp.request.UserPwdChangReq;
import com.techlambdas.delearmanagementapp.request.UserReq;
import com.techlambdas.delearmanagementapp.request.UserUpdateReq;
import com.techlambdas.delearmanagementapp.response.LoginResponse;
import com.techlambdas.delearmanagementapp.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserCustomRepository userCustomRepository;
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Override
    public String changePassword(UserPwdChangReq userPwdChangReq) {
        User user=userRepository.findUserByMobileNumber(userPwdChangReq.getMobileNo());
        if (user == null)
            throw new DataNotFoundException("Invalid mobileNo. Please give valid MobileNo.");
        if (bCryptPasswordEncoder.matches(userPwdChangReq.getOldPassword(), user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(userPwdChangReq.getNewPassword()));
            user.setPasswordReset(true);
            userRepository.save(user);
            return "Password Changed SuccessFully";
        }
        else {
            throw new DataNotFoundException("Old Password Not Match:"+userPwdChangReq.getOldPassword());
        }

    }

    @Override
    public LoginResponse authenticateUser(LoginReq loginReq) {
        User user=userRepository.findUserByMobileNumber(loginReq.getMobileNo());
        if (user == null)
            throw new DataNotFoundException("Invalid mobileNo. Please give valid MobileNo.");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), loginReq.getPassWord()));
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid password. Please try again.");
        }
        if (user.getUserStatus().equals(UserStatus.INACTIVE)){
            throw new InvalidDataException("Invalid User This User is Deactivated");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
        String token = jwtUtils.generateJwtToken(userDetails);
        LoginResponse loginResponse=new LoginResponse();
        loginResponse.setUserId(user.getUserId());
        loginResponse.setUserName(user.getUserName());
        loginResponse.setDesignation(user.getDesignation());
        loginResponse.setToken(token);
        loginResponse.setPasswordReset(user.isPasswordReset());
        loginResponse.setUseRefId(user.getUseRefId());
        loginResponse.setBranchId(user.getBranchId());
        return loginResponse;
    }

    @Override
    public User createUser(UserReq userReq) {
        User user=new User();
        if (userReq.getUserName()!=null){
            Optional<User> existingUser =userRepository.findByUserName(userReq.getUserName());
            if (existingUser.isPresent())
                throw new AlreadyExistException("UserName Already Exist:" + userReq.getUserName());
        }
        if (userReq.getMobileNo()!=null) {
            User existingUser = userRepository.findUserByMobileNumber(userReq.getMobileNo());
            if (existingUser != null)
                throw new AlreadyExistException("MobileNo Already Exist:" + userReq.getMobileNo());
        }
        user.setUserId(RandomIdGenerator.getRandomId());
        user.setUserName(userReq.getUserName());
        user.setMobileNumber(userReq.getMobileNo());
        user.setDesignation(userReq.getDesignation());
        user.setUseRefId(userReq.getUseRefId());
        user.setUserStatus(UserStatus.ACTIVE);
        user.setPasswordReset(false);
        user.setBranchId(userReq.getBranchId());
       user.setPassword(bCryptPasswordEncoder.encode(userReq.getPassWord()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UserUpdateReq userUpdateReq) {
        User user=userRepository.findUserByMobileNumber(userUpdateReq.getMobileNo());
        if (user==null)
            throw new DataNotFoundException("user Not found this MobileNo:"+userUpdateReq.getMobileNo());
        user.setUserName(userUpdateReq.getUserName());
        user.setMobileNumber(userUpdateReq.getMobileNo());
        user.setDesignation(userUpdateReq.getDesignation());
        return userRepository.save(user);
    }

    @Override
    public User getUserByMobileNo(String mobileNo) {
        User user=userRepository.findUserByMobileNumber(mobileNo);
        if (user==null)
            throw new DataNotFoundException("user Not found this MobileNo:"+mobileNo);
        return user;
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
    @Override
    public String getUserNameByUserId(String createdBy) {
        User user=userRepository.findUserByUserId(createdBy);
        return user.getUserName();
    }

    @Override
    public Page<User> getUsersByPagination(String userName, String mobileNumber, String designation, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize);
        Page<User> userList = userCustomRepository.getAllUsersWithPage(userName,mobileNumber,designation,pageable);
        return userList;
    }

    @Override
    public void deleteUser(String userId) {
        User user=userRepository.findUserByUserId(userId);
        if (user==null)
            throw new DataNotFoundException("user Not found this Id:"+userId);
        userRepository.delete(user);
    }

    @Override
    public String changeUserStatus(String userId, UserStatus status) {
        User user=userRepository.findUserByUserId(userId);
        if (user==null)
            throw new DataNotFoundException("User Not Found with"+userId);
        user.setUserStatus(status);
        userRepository.save(user);
        return "User Status changed Successfully";
    }

    @Override
    public String resetPassword(String userId) {
        User user=userRepository.findUserByUserId(userId);
        if (user==null)
            throw new DataNotFoundException("User Not Found with"+userId);
        user.setPassword(bCryptPasswordEncoder.encode("1234"));
        user.setPasswordReset(false);
       userRepository.save(user);
       return "Password Reset Successfully";
    }

}
