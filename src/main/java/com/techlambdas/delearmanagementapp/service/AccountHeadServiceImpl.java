package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.constant.PricingFormat;
import com.techlambdas.delearmanagementapp.exception.AlreadyExistException;
import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.AccountHeadMapper;
import com.techlambdas.delearmanagementapp.model.AccountHead;
import com.techlambdas.delearmanagementapp.repository.AccountHeadRepository;
import com.techlambdas.delearmanagementapp.request.AccountHeadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountHeadServiceImpl implements AccountHeadService {

   @Autowired
   private AccountHeadMapper accountHeadMapper;

    @Autowired
    private AccountHeadRepository accountHeadRepository;


    @Override
    public AccountHead createAccountHead(AccountHeadRequest accountHeadRequest) {
        try {
            boolean exist = accountHeadRepository.existsByAccountHeadCode(accountHeadRequest.getAccountHeadCode());
            if (exist)
                throw new AlreadyExistException("Account head code Already exist");
            AccountHead accountHead = accountHeadMapper.mapAccountHeadRequestToAccountHead(accountHeadRequest);
            return accountHeadRepository.save(accountHead);
        } catch (AlreadyExistException ex) {
            throw new AlreadyExistException("Code already exist --" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }

    }



    @Override
    public Page<AccountHead> searchAccountHead(int page, int size, String accountHeadCode, String accountHeadName, PricingFormat pricingFormat, Boolean isCashierOps, boolean activeStatus, AccountType accountType, String transferFrom) {
       Page<AccountHead> accountHeads =  accountHeadRepository.searchAccountHead(page,size,accountHeadCode,accountHeadName,pricingFormat,isCashierOps,activeStatus,accountType,transferFrom);
       return accountHeads;
    }

    @Override
    public AccountHead getAccountHead(String accountHeadCode) {
        try {
            AccountHead accountHead = accountHeadRepository.findByAccountHeadCode(accountHeadCode);
            if (accountHead == null)
                throw new DataNotFoundException("Account not found");
            return accountHead;
        }catch (DataNotFoundException ex) {
            throw new DataNotFoundException("Acount not found --" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public String checkAccountHeadCode(String accountHeadCode) {
        try {
            String response;
            boolean exist = accountHeadRepository.existsByAccountHeadCode(accountHeadCode);
            if (exist)
                return "CodeExist!";
            return "CodeAvailable";
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public AccountHead updateAccountHead(String id, AccountHeadRequest ahreq) {
        try{
            AccountHead accountHead = accountHeadRepository.findById(id).get();
            if(accountHead == null){
                throw new DataNotFoundException("Account head not found --Invalid accounthead id");
            }
           accountHeadMapper.updateExistingAccountHead(ahreq,accountHead);
            return  accountHeadRepository.save(accountHead);
        }catch (DataNotFoundException ex) {
            throw new DataNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }

    }

    @Override
    public boolean deleteAccountHead(String id) {
        boolean deleteFlag = false;
        try{
            accountHeadRepository.deleteById(id);
            deleteFlag = true;
            return deleteFlag;
        }catch (DataNotFoundException ex) {
            throw new DataNotFoundException(ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }

    }



    @Override
    public List<AccountHead> getAllAccHead(AccountType accountType) {
        if(accountType==null) {
            return accountHeadRepository.findAll();
        }else
        {
            return accountHeadRepository.findByAccountType(accountType);
        }
    }


}
