package com.techlambdas.delearmanagementapp.controller;

import com.techlambdas.delearmanagementapp.model.Receipt;
import com.techlambdas.delearmanagementapp.request.ReceiptRequest;
import com.techlambdas.delearmanagementapp.response.ReceiptResponse;
import com.techlambdas.delearmanagementapp.service.ReceiptService;
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

import static com.techlambdas.delearmanagementapp.response.AppResponse.successResponse;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {
    @Autowired
    private ReceiptService receiptService;

    @PostMapping
    public ResponseEntity<Receipt> createReceipt(@RequestBody ReceiptRequest receiptRequest)
    {
        Receipt receipt=receiptService.createReceipt(receiptRequest);
        return successResponse(HttpStatus.CREATED,"receipt",receipt);
    }
    @GetMapping
    public ResponseEntity<List<ReceiptResponse>> getAllReceipts(@RequestParam(required = false) String receiptId,
                                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                @RequestParam(required = false)  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate)
    {
        List<ReceiptResponse> receipts=receiptService.getAllReceipts(receiptId,fromDate,toDate);
        return successResponse(HttpStatus.OK,"receipts",receipts);
    }
    @PutMapping("/{receiptId}")
    public ResponseEntity<Receipt> updateReceipt(@PathVariable String receiptId,@RequestBody ReceiptRequest receiptRequest)
    {
        Receipt receipt=receiptService.updateReceipt(receiptId,receiptRequest);
        return successResponse(HttpStatus.OK,"UpdatedReceipt",receipt);
    }
    @GetMapping("/page")
    public ResponseEntity<Page<Receipt>> getAllReceiptWithPage(@RequestParam(required = false) String receiptId,
                                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                               @RequestParam(required = false)  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size)
    {
        Pageable pageable= PageRequest.of(page, size);
        Page<ReceiptResponse> receiptPage=receiptService.getAllReceiptsWithPage(receiptId,fromDate,toDate, pageable);
        return successResponse(HttpStatus.OK,"receiptWithPage",receiptPage);
    }
    @GetMapping("/{receiptId}")
    public ResponseEntity<Receipt> getReceiptById(@PathVariable String receiptId)
    {
        Receipt receipt=receiptService.getReceiptByReceiptId(receiptId);
        return successResponse(HttpStatus.OK,"receiptById",receipt);
    }
}
