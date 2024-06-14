package com.techlambdas.delearmanagementapp.controller;

import com.techlambdas.delearmanagementapp.constant.TransferStatus;
import com.techlambdas.delearmanagementapp.constant.TransferType;
import com.techlambdas.delearmanagementapp.model.Stock;
import com.techlambdas.delearmanagementapp.request.StockAddReq;
import com.techlambdas.delearmanagementapp.request.StockRequest;
import com.techlambdas.delearmanagementapp.request.TransferRequest;
import com.techlambdas.delearmanagementapp.response.StockResponse;
import com.techlambdas.delearmanagementapp.response.TransferResponse;
import com.techlambdas.delearmanagementapp.service.StockService;
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
@RequestMapping("/stock")
public class StockController {
    @Autowired
    private StockService stockService;
    @PostMapping
    public ResponseEntity<Stock> createStock(@RequestBody StockRequest stockRequest) {
        Stock stock = stockService.createStock(stockRequest);
        return successResponse(HttpStatus.CREATED,"stock",stock);
    }
    @GetMapping
    public ResponseEntity<List<StockResponse>> getAllStockS(@RequestParam(required = false) String partNo,
                                                            @RequestParam(required = false) String itemName,
                                                            @RequestParam(required = false) String keyValue,

                                                            @RequestParam(required = false) String categoryName,
                                                            @RequestParam(required = false) String branchId){
        List<StockResponse> stocks=stockService.getAllStocks(partNo,itemName,keyValue,categoryName);
        return successResponse(HttpStatus.OK,"Stocks",stocks);
    }
//    @PutMapping("/{id}")
//    public ResponseEntity<Stock> updateStockDetails(@PathVariable String id,@RequestBody StockRequest stockRequest)
//    {
//        Stock stock=stockService.updateStockDetails(id,stockRequest);
//        return successResponse(HttpStatus.OK,"UpdatedStock",stock);
//    }
    @GetMapping("/page")
    public ResponseEntity<Page<StockResponse>> getAllStockWithPage(@RequestParam(required = false) String partNo,
                                                           @RequestParam(required = false) String itemName,
                                                           @RequestParam(required = false) String keyValue,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                                   @RequestParam(required = false) String categoryName,
                                                                   @RequestParam(required = false) String branchId)
    {
        Pageable pageable = PageRequest.of(page,size);
        Page<StockResponse> stockPage = stockService.getAllStocksWithPage(partNo,itemName,keyValue, pageable,categoryName);
        return successResponse(HttpStatus.OK,"stockWithPage",stockPage);
    }
    @PatchMapping("/{purchaseId}")
    public ResponseEntity<List<StockResponse>> createStockFromPurchase(@PathVariable String purchaseId,
                                                                       @RequestBody StockAddReq stockAddReq)
    {
        List<StockResponse> stockResponses=stockService.createStockFromPurchase(purchaseId,stockAddReq);
        return successResponse(HttpStatus.CREATED,"stock",stockResponses);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> createTransfer(@RequestBody TransferRequest transferRequest) {
        String result = stockService.createTransfer(transferRequest);
        return successResponse(HttpStatus.CREATED,"success",result);
    }
    @GetMapping("/transferd")
    public ResponseEntity<List<TransferResponse>>getTransferDetails(@RequestParam  (required = false)String fromBranchId,
                                                                    @RequestParam  (required = false)String toBranchId,
                                                                    @RequestParam(required = false) TransferStatus transferStatus,
                                                                    @RequestParam(required = false) TransferType transferType) {
      List<TransferResponse> transferResponses = stockService.getTransferDetails(fromBranchId,toBranchId,transferStatus,transferType);
        return successResponse(HttpStatus.OK,"transferDetails",transferResponses);
    }
    @PatchMapping("/transfer/approve")
    public ResponseEntity<String>approveTransfer(@RequestParam  String branchId, @RequestParam String transferId) {
        String result= stockService.approveTransfer(branchId,transferId);
        return successResponse(HttpStatus.OK,"success",result);
    }
}
