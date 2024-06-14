package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.CommonMapper;
import com.techlambdas.delearmanagementapp.mapper.SalesMapper;
import com.techlambdas.delearmanagementapp.model.*;
import com.techlambdas.delearmanagementapp.model.Sales;
import com.techlambdas.delearmanagementapp.repository.CustomSalesRepository;
import com.techlambdas.delearmanagementapp.repository.SalesRepository;
import com.techlambdas.delearmanagementapp.repository.StockRepository;
import com.techlambdas.delearmanagementapp.request.ItemDetailRequest;
import com.techlambdas.delearmanagementapp.request.SalesRequest;
import com.techlambdas.delearmanagementapp.request.SalesUpdateReq;
import com.techlambdas.delearmanagementapp.response.SalesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalesServiceImpl implements  SalesService{

    @Autowired
    private SalesMapper salesMapper;

    @Autowired
    private SalesRepository salesRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockService stockService;

    @Autowired
    private CustomSalesRepository customSalesRepository;

    @Autowired
    private ConfigService configService;
    @Autowired
    private StockService stockService;


    @Autowired
    private CommonMapper commonMapper;

    @Override
    public SalesResponse createSales(SalesRequest salesRequest) {
        try {
            Sales sales = salesMapper.mapSalesRequestToSales(salesRequest);
            stockService.mapSalesRequestToStock(salesRequest);
            sales.setInvoiceNo(configService.getNextSalesNoSequence());
            Sales createdSales= salesRepository.save(sales);
            stockService.updateSalesInfoToStock(createdSales);
            return commonMapper.toSalesResponse(createdSales);
        }
        catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public List<SalesResponse> getAllSales(String invoiceNo) {
        List<Sales> sales = customSalesRepository.getAllSales(invoiceNo);
        return sales.stream()
                .map(commonMapper::toSalesResponse)
                .collect(Collectors.toList());
//        return salesRepository.findAll();
    }

    @Override
    public Sales updateSales(SalesUpdateReq salesUpdateReq) {
        Sales sales = salesRepository.findByCustomerId(salesUpdateReq.getCustomerId());
        System.out.println(sales+"-------------->");
        Sales sale = salesMapper.mapSalesUpdateReqtoSales(salesUpdateReq);
        System.out.println(sale+"--------sale from impl class------>");
        return salesRepository.save(sale);
    }

    @Override
    public void deleteSales(String invoiceNo) {
        Sales sales = salesRepository.findByInvoiceNo(invoiceNo);
        salesRepository.delete(sales);
    }

    @Override
    public SalesResponse getSalesByInvoiceNo(String invoiceNo) {
        Sales  sales = salesRepository.findByInvoiceNo(invoiceNo);
        if (sales==null)
            throw new DataNotFoundException("not found with ID:  " + invoiceNo);
        return commonMapper.toSalesResponse(sales);
    }

    @Override
    public List<SalesResponse> getAllSalesView(String invoiceNo) {
        List<Sales> sales = customSalesRepository.getAllSales(invoiceNo);
        return sales.stream()
                .map(commonMapper::toSalesResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<SalesResponse> getAllSalesWithPage(String invoiceNo, String categoryName , LocalDate fromDate , LocalDate toDate, Pageable pageable) {
        Page<Sales>sales= customSalesRepository.getAllSalesWithPage(invoiceNo, categoryName ,fromDate , toDate ,pageable);
        List<SalesResponse>salesResponses=sales.stream()
                .map(commonMapper::toSalesResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(salesResponses,pageable,sales.getTotalElements());
    }


}
