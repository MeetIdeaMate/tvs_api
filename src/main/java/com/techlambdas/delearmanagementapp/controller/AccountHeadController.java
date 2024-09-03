package com.techlambdas.delearmanagementapp.controller;

import com.techlambdas.delearmanagementapp.constant.AccountType;
import com.techlambdas.delearmanagementapp.constant.PricingFormat;
import com.techlambdas.delearmanagementapp.model.AccountHead;
import com.techlambdas.delearmanagementapp.request.AccountHeadRequest;
import com.techlambdas.delearmanagementapp.service.AccountHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.techlambdas.delearmanagementapp.response.AppResponse.successResponse;


@RestController
@RequestMapping(path = "/acchead")
public class AccountHeadController {

    @Autowired
    private AccountHeadService accountHeadService;

    @PostMapping
    public ResponseEntity createAccountHead(@RequestBody AccountHeadRequest accountHeadRequest){
        AccountHead accountHead = accountHeadService.createAccountHead(accountHeadRequest);
        return successResponse(HttpStatus.CREATED,"accountHead",accountHead);
    }


    @GetMapping("/allAccHead")
    public ResponseEntity getAllAccHead(@RequestParam(required = false) AccountType accountType){
       List <AccountHead> accountHead = accountHeadService.getAllAccHead(accountType);
       return successResponse(HttpStatus.OK,"accountHead",accountHead);
    }

    @GetMapping
    public ResponseEntity getAccountHeadBySearch(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String transferFrom,
            @RequestParam(required = false) String accountHeadCode, @RequestParam(required = false) String accountHeadName,
            @RequestParam(required = false) PricingFormat pricingFormat, @RequestParam(required = false) Boolean isCashierOps,
            @RequestParam(defaultValue = "true") boolean activeStatus, @RequestParam(required = false) AccountType accountType){
        Page<AccountHead> accountHeads = accountHeadService.searchAccountHead(page,size,accountHeadCode,accountHeadName,pricingFormat,isCashierOps,activeStatus,accountType,transferFrom);
        return successResponse(HttpStatus.OK, "accountHead", accountHeads);
    }

    @GetMapping("/{accountHeadCode}")
    public ResponseEntity getAccountHead(@PathVariable String accountHeadCode){
        AccountHead accountHead = accountHeadService.getAccountHead(accountHeadCode);
        return successResponse(HttpStatus.OK,"accountHead",accountHead);
    }

    @GetMapping("/check")
    public ResponseEntity checkAccountCode(@RequestParam String accuntHeadCode){
        String response = accountHeadService.checkAccountHeadCode(accuntHeadCode);
        return successResponse(HttpStatus.OK,"Availability",response);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateAccountHead(@PathVariable String id,@RequestBody AccountHeadRequest accountHeadRequest){
        AccountHead accountHead = accountHeadService.updateAccountHead(id,accountHeadRequest);
        return successResponse(HttpStatus.OK,"accountHead",accountHead);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAccountHead(@PathVariable String id){
        boolean accountHead = accountHeadService.deleteAccountHead(id);
        return successResponse(HttpStatus.OK,"accountHead",accountHead);
    }


}
