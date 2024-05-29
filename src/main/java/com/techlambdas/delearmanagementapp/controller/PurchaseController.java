package com.techlambdas.delearmanagementapp.controller;

import com.techlambdas.delearmanagementapp.model.Purchase;
import com.techlambdas.delearmanagementapp.request.PurchaseRequest;
import com.techlambdas.delearmanagementapp.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.techlambdas.delearmanagementapp.response.AppResponse.successResponse;

@RestController
@RequestMapping("/purchase")
public class PurchaseController
{
    @Autowired
    private PurchaseService purchaseService;
    @PostMapping
    public ResponseEntity<Purchase> createPurchase(@RequestBody PurchaseRequest purchaseRequest) {
        Purchase purchase = purchaseService.createPurchase(purchaseRequest);
        return successResponse(HttpStatus.CREATED,"purchase",purchase);
    }
    @GetMapping
    public ResponseEntity<List<Purchase>>getAllPurchases(@RequestParam(required = false) String purchaseNo,
                                                     @RequestParam(required = false) String p_invoiceNo,
                                                     @RequestParam(required = false) String p_orderRefNo){
        List<Purchase> purchases=purchaseService.getAllPurchases(purchaseNo,p_invoiceNo,p_orderRefNo);
        return new ResponseEntity<>(purchases, HttpStatus.OK);
    }
    @PutMapping("/{purchaseNo}")
    public ResponseEntity<Purchase> updatePurchase(@PathVariable String purchaseNo,@RequestBody PurchaseRequest purchaseRequest){
        Purchase purchase=purchaseService.updatePurchase(purchaseNo,purchaseRequest);
        return new ResponseEntity<>(purchase, HttpStatus.OK);
    }
    @GetMapping("/page")
    public ResponseEntity<Page<Purchase>> getAllPurchaseWithPage(
            @RequestParam(required = false) String purchaseNo,
            @RequestParam(required = false) String p_invoiceNo,
            @RequestParam(required = false) String p_orderRefNo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Purchase> purchasesPage = purchaseService.getAllPurchasesWithPage(purchaseNo,p_invoiceNo,p_orderRefNo, pageable);

        return successResponse(HttpStatus.OK,"purchasesWithPage",purchasesPage);
    }
}
