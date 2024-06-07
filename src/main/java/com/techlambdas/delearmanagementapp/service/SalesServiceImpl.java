package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.SalesMapper;
import com.techlambdas.delearmanagementapp.model.Sales;
import com.techlambdas.delearmanagementapp.repository.SalesRepository;
import com.techlambdas.delearmanagementapp.request.SalesRequest;
import com.techlambdas.delearmanagementapp.request.SalesUpdateReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesServiceImpl implements  SalesService{

    @Autowired
    private SalesMapper salesMapper;

    @Autowired
    private SalesRepository salesRepository;
    @Autowired
    private CustomerService customerService;

    @Override
    public Sales createSales(SalesRequest salesRequest) {
        try {
            Sales sales = salesMapper.mapSalesRequestToSales(salesRequest);
            return salesRepository.save(sales);
        }
        catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public List<Sales> getAllSales() {
        return salesRepository.findAll();
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
    public Sales getSalesByInvoiceNo(String invoiceNo) {
        Sales  sales = salesRepository.findByInvoiceNo(invoiceNo);
        if (sales==null)
            throw new DataNotFoundException("not found with ID:  " + invoiceNo);
        return sales;
    }

}
