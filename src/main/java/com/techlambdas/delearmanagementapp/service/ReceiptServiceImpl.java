package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.CommonMapper;
import com.techlambdas.delearmanagementapp.mapper.ReceiptMapper;
import com.techlambdas.delearmanagementapp.model.Receipt;
import com.techlambdas.delearmanagementapp.repository.CustomReceiptRepository;
import com.techlambdas.delearmanagementapp.repository.ReceiptRepository;
import com.techlambdas.delearmanagementapp.request.ReceiptRequest;
import com.techlambdas.delearmanagementapp.response.ReceiptResponse;
import com.techlambdas.delearmanagementapp.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReceiptServiceImpl implements ReceiptService{
    @Autowired
    private ReceiptMapper receiptMapper;
    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private CustomReceiptRepository customReceiptRepository;
    @Autowired
    private ConfigService configService;
    @Autowired
    private CommonMapper commonMapper;

    @Override
    public Receipt createReceipt(ReceiptRequest receiptRequest) {
        try {
            Receipt receipt=receiptMapper.mapReceiptRequestToReceipt(receiptRequest);
            receipt.setReceiptId(RandomIdGenerator.getRandomId());
            receipt.setReceiptNo(configService.getNextReceiptNoSequence());
            return receiptRepository.save(receipt);
        }catch (Exception ex){
            throw new RuntimeException("Internal Server Error --"+ex.getMessage(),ex.getCause());
        }
    }

    @Override
    public List<ReceiptResponse> getAllReceipts(String receiptId, LocalDate fromDate, LocalDate toDate) {
        List<Receipt> receipts=customReceiptRepository.getAllReceipts(receiptId,fromDate,toDate);
        return receipts.stream()
                .map(commonMapper::ToReceiptResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Receipt updateReceipt(String receiptId, ReceiptRequest receiptRequest) {
        try {
            Receipt existingReceipt = receiptRepository.findByReceiptId(receiptId);
            if (!Optional.ofNullable(existingReceipt).isPresent())
                throw new DataNotFoundException("Receipt not found with this ID :"+receiptId);
            receiptMapper.updateReceiptFromRequest(receiptRequest,existingReceipt);
            return receiptRepository.save(existingReceipt);
        }catch (DataNotFoundException ex)
        {
            throw new DataNotFoundException("Data not found -- "+ex.getMessage());
        }catch (Exception ex){
            throw new RuntimeException("Internal Server Error -- "+ex.getMessage(),ex.getCause());
        }
    }

    @Override
    public Page<ReceiptResponse> getAllReceiptsWithPage(String receiptId, LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        Page<Receipt> receipts= customReceiptRepository.getAllReceiptsWithPage(receiptId,fromDate,toDate,pageable);
        List<ReceiptResponse> receiptResponses=receipts.stream()
                .map(commonMapper::ToReceiptResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(receiptResponses,pageable,receipts.getTotalElements());
    }

    @Override
    public Receipt getReceiptByReceiptId(String receiptId) {
        Receipt receipt=receiptRepository.findByReceiptId(receiptId);
        if (receipt==null)
            throw new DataNotFoundException("Receipt not found with ID :" + receiptId);
        return receipt;
    }
}
