package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.constant.BookingStatus;
import com.techlambdas.delearmanagementapp.constant.PaymentType;
import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.CommonMapper;
import com.techlambdas.delearmanagementapp.mapper.SalesMapper;
import com.techlambdas.delearmanagementapp.model.*;
import com.techlambdas.delearmanagementapp.model.Sales;
import com.techlambdas.delearmanagementapp.repository.BookingRepository;
import com.techlambdas.delearmanagementapp.repository.CustomSalesRepository;
import com.techlambdas.delearmanagementapp.repository.SalesRepository;
import com.techlambdas.delearmanagementapp.repository.StockRepository;
import com.techlambdas.delearmanagementapp.request.PaidDetailReq;
import com.techlambdas.delearmanagementapp.request.SalesRequest;
import com.techlambdas.delearmanagementapp.request.SalesUpdateReq;
import com.techlambdas.delearmanagementapp.response.SalesResponse;
import com.techlambdas.delearmanagementapp.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private BookingRepository bookingRepository;
    @Autowired
    private CommonMapper commonMapper;

    @Override
    public SalesResponse createSales(SalesRequest salesRequest) {
        try {
            Sales sales = salesMapper.mapSalesRequestToSales(salesRequest);
            sales.setInvoiceNo(configService.getNextSalesNoSequence());
            for (PaidDetail paidDetail:sales.getPaidDetails())
            {
                List<PaidDetail> newList = new ArrayList<>();
                paidDetail.setPaymentId(RandomIdGenerator.getRandomId());
                newList.add(paidDetail);
                sales.setPaidDetails(newList);
            }
            double totalCgst = 0;
            double totalSgst=0;
            double totalIgst=0;
            for (ItemDetail itemDetail:sales.getItemDetails())
            {
                for (GstDetail gstDetail : itemDetail.getGstDetails()) {
                    GstType gstName = gstDetail.getGstName();
                    if (gstName == GstType.CGST) {
                        totalCgst += gstDetail.getGstAmount();
                    } else if (gstName == GstType.SGST) {
                        totalSgst += gstDetail.getGstAmount();
                    }else if (gstName==GstType.IGST){
                        totalIgst=gstDetail.getGstAmount();
                    }
                }
                sales.setTotalTaxableAmt(itemDetail.getTaxableValue());

                for (Incentive incentive:itemDetail.getIncentives())
                {
                    sales.setTotalIncentiveAmount(incentive.getIncentiveAmount());
                }
                sales.setTotalInvoiceAmt(itemDetail.getFinalInvoiceValue());
                sales.setTotalDisc(itemDetail.getDiscount());
            }
            List<PaidDetail> paidDetail= new ArrayList<>();
            Booking booking=bookingRepository.findByBookingNo(salesRequest.getBookingNo());
            if (booking !=null && booking.getPaidDetail()!=null)
            {
                PaidDetail bookingPaidDetail=booking.getPaidDetail();
                PaidDetail newPaidDetail=new PaidDetail();
                newPaidDetail.setPaymentId(bookingPaidDetail.getPaymentId());
                newPaidDetail.setPaymentDate(bookingPaidDetail.getPaymentDate());
                newPaidDetail.setPaidAmount(bookingPaidDetail.getPaidAmount());
                newPaidDetail.setPaymentType(PaymentType.ADVANCE);
                paidDetail.add(newPaidDetail);
                booking.setBookingStatus(BookingStatus.COMPLETED);
                bookingRepository.save(booking);
            }
            paidDetail.addAll(salesRequest.getPaidDetails());
            sales.setPaidDetails(paidDetail);
            double paidAmount = sales.getPaidDetails().stream()
                    .mapToDouble(pd -> pd.getPaidAmount())
                    .sum();
            if (paidAmount==sales.getNetAmt()){
                sales.setPaymentStatus(PaymentStatus.COMPLETED);
            }else {
                sales.setPaymentStatus(PaymentStatus.PENDING);
            }
            sales.setTotalCgst(totalCgst);
            sales.setTotalSgst(totalSgst);
            sales.setTotalIgst(totalIgst);
            Sales createdSales= salesRepository.save(sales);
            stockService.updateSalesInfoToStock(createdSales);
            return commonMapper.toSalesResponse(createdSales);
        }
        catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }
    @Override
    public List<SalesResponse> getAllSales(String invoiceNo,String customerName,String mobileNo ,String partNo, String paymentType,Boolean isCancelled,PaymentStatus paymentStatus,String billType) {
        List<Sales> sales = customSalesRepository.getAllSales(invoiceNo, customerName, mobileNo, partNo, paymentType,isCancelled,paymentStatus,billType);
        return sales.stream()
                .map(sale -> {
                    double balance = 0;
                    double paidAmount = 0;
                    for (PaidDetail paidDetail : sale.getPaidDetails()) {
                        if (!paidDetail.isCancelled())                        {
                            paidAmount = paidAmount + paidDetail.getPaidAmount();
                        }
                    }
                    balance = sale.getNetAmt() - paidAmount;
                    SalesResponse salesResponse = commonMapper.toSalesResponse(sale);
                    salesResponse.setPendingAmt(balance);
                    salesResponse.setTotalPaidAmt(paidAmount);
                    return salesResponse;
                }).collect(Collectors.toList());
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

//    @Override
//    public List<SalesResponse> getAllSalesView(String invoiceNo) {
//        List<Sales> sales = customSalesRepository.getAllSales(invoiceNo);
//        return sales.stream()
//                .map(commonMapper::toSalesResponse)
//                .collect(Collectors.toList());
//    }

    @Override
    public Page<SalesResponse> getAllSalesWithPage(String invoiceNo, String categoryName ,String customerName,String mobileNo,String partNo,String paymentType,Boolean isCancelled,String branchName,String branchId,String billType,PaymentStatus paymentStatus,LocalDate fromDate , LocalDate toDate, Pageable pageable) {
        Page<Sales>sales= customSalesRepository.getAllSalesWithPage(invoiceNo, categoryName,customerName,mobileNo,partNo,paymentType,isCancelled,branchName,branchId,billType,paymentStatus,fromDate , toDate ,pageable);
        List<SalesResponse>salesResponses=sales.stream()
                .map(sale -> {
                    double balance = 0;
                    double paidAmount = 0;
                    for (PaidDetail paidDetail : sale.getPaidDetails()) {
                        if (!paidDetail.isCancelled())                        {
                            paidAmount = paidAmount + paidDetail.getPaidAmount();
                        }
                    }
                    balance = sale.getNetAmt() - paidAmount;

                    SalesResponse salesResponse = commonMapper.toSalesResponse(sale);
                    salesResponse.setPendingAmt(balance);
                    salesResponse.setTotalPaidAmt(paidAmount);
                    return salesResponse;
                }).collect(Collectors.toList());
        return new PageImpl<>(salesResponses,pageable,sales.getTotalElements());
    }

    @Override
    public String updatePaymentDetails(String salesId, PaidDetailReq paidDetailReq) {
        Sales sales = salesRepository.findBySalesId(salesId);
        if (sales == null) {
            throw new RuntimeException("Sales not found with id: " + salesId);
        }
        PaidDetail paidDetail = new PaidDetail();
        paidDetail.setPaidAmount(paidDetailReq.getPaidAmount());
        paidDetail.setPaymentDate(paidDetailReq.getPaymentDate());
        paidDetail.setPaymentType(paidDetailReq.getPaymentType());
        paidDetail.setPaymentId(RandomIdGenerator.getRandomId());
        if (sales.getPaidDetails()!=null)
        {
            double paidAmount = sales.getPaidDetails()
                    .stream()
                    .filter(pd->!pd.isCancelled())
                    .mapToDouble(pd->pd.getPaidAmount())
                    .sum();
            if (paidAmount + paidDetailReq.getPaidAmount() > sales.getTotalInvoiceAmt()) {
                return "Paid Amount is greater than total invoice amount";
            }
            else
            {
                sales.getPaidDetails().add(paidDetail);
            }
        }else
        {
            List<PaidDetail> paidDetailList = new ArrayList<>();
            paidDetailList.add(paidDetail);
            sales.setPaidDetails(paidDetailList);
        }
        double paidAmount = sales.getPaidDetails().stream()
                .mapToDouble(pd -> pd.getPaidAmount())
                .sum();
        if (paidAmount == sales.getNetAmt()) {
            sales.setPaymentStatus(PaymentStatus.COMPLETED);}
        salesRepository.save(sales);
        return "payment successful";
    }

    @Override
    public String cancelPaymentDetails(String salesId, String paymentId,String reason) {
        Sales sales = salesRepository.findBySalesId(salesId);
        if (sales == null) {
            throw new RuntimeException("Sales not found with id: " + salesId);
        }
        for (PaidDetail paidDetail : sales.getPaidDetails()) {
            if (paidDetail.getPaymentId().equals(paymentId)) {
                paidDetail.setCancelled(true);
                paidDetail.setReason(reason);
                break;
            }
        }
        salesRepository.save(sales);
        return "payment cancelled";
    }

    @Override
    public String cancelSales(String salesId, String reason) {
        Sales sales = salesRepository.findBySalesId(salesId);
        if (sales == null) {
            throw new RuntimeException("Sales not found with id: " + salesId);
        }
        if (sales.getSalesId().equals(salesId))
        {
            sales.setCancelled(true);
            sales.setReason(reason);
        }
        Sales cancelSales=salesRepository.save(sales);
        stockService.salesCancelInfoToStock(cancelSales);
        return "Sales cancelled";
    }
}
