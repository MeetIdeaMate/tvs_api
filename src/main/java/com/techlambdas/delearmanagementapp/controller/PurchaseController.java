package com.techlambdas.delearmanagementapp.controller;
import com.techlambdas.delearmanagementapp.model.Purchase;
import com.techlambdas.delearmanagementapp.request.PurchaseRequest;
import com.techlambdas.delearmanagementapp.response.ItemDetailResponse;
import com.techlambdas.delearmanagementapp.response.PurchaseResponse;
import com.techlambdas.delearmanagementapp.service.ItemService;
import com.techlambdas.delearmanagementapp.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.techlambdas.delearmanagementapp.response.AppResponse.successResponse;

@RestController
@RequestMapping("/purchase")
public class PurchaseController
{
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private ItemService itemService;
    @PostMapping
    public ResponseEntity<PurchaseResponse> createPurchase(@RequestBody PurchaseRequest purchaseRequest) {
        PurchaseResponse purchase = purchaseService.createPurchase(purchaseRequest);
        return successResponse(HttpStatus.CREATED,"purchase",purchase);
    }
    @GetMapping
    public ResponseEntity<List<PurchaseResponse>>getAllPurchases(@RequestParam(required = false) String purchaseNo,
                                                     @RequestParam(required = false) String p_invoiceNo,
                                                     @RequestParam(required = false) String p_orderRefNo,
                                                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                 @RequestParam(required = false)  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                                 @RequestParam(required = false) String categoryName,
                                                                 @RequestParam(required = false) String branchId){
        List<PurchaseResponse> purchases=purchaseService.getAllPurchases(purchaseNo,p_invoiceNo,p_orderRefNo,fromDate,toDate,categoryName,branchId);
        return successResponse(HttpStatus.CREATED,"purchases",purchases);
    }
    @PutMapping("/{purchaseNo}")
    public ResponseEntity<Purchase> updatePurchase(@PathVariable String purchaseNo,@RequestBody PurchaseRequest purchaseRequest){
        Purchase purchase=purchaseService.updatePurchase(purchaseNo,purchaseRequest);
        return successResponse(HttpStatus.CREATED,"Updatepurchase",purchase);
    }
    @GetMapping("/page")
    public ResponseEntity<Page<PurchaseResponse>> getAllPurchaseWithPage(
            @RequestParam(required = false) String purchaseNo,
            @RequestParam(required = false) String p_invoiceNo,
            @RequestParam(required = false) String p_orderRefNo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false)  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String branchId
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PurchaseResponse> purchasesPage = purchaseService.getAllPurchasesWithPage(purchaseNo,p_invoiceNo,p_orderRefNo, pageable,fromDate,toDate,categoryName,branchId);

        return successResponse(HttpStatus.OK,"purchasesWithPage",purchasesPage);
    }
    @GetMapping("/ByPartNo/{partNo}")
    public ResponseEntity<ItemDetailResponse> getPurchasesByPartNo(@PathVariable String partNo) {
        ItemDetailResponse itemDetail = purchaseService.getItemDetailsByPartNo(partNo);
        return successResponse(HttpStatus.OK,"ItemDetailByPartNo",itemDetail);
    }
    @PatchMapping("/cancel/{purchaseId}")
    public ResponseEntity<String> cancelPurchase(@PathVariable String purchaseId){
        String result=purchaseService.cancelPurchase(purchaseId);
        return successResponse(HttpStatus.CREATED,"success",result);
    }
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validatePurchaseItem(@RequestParam String partNo,@RequestBody Map<String,String> mainSpecValue) {
        Boolean isExist = purchaseService.validatePurchaseItem(partNo,mainSpecValue);
        return successResponse(HttpStatus.OK,"successResponse",isExist);
    }
}
