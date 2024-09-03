package com.techlambdas.delearmanagementapp.controller;

import com.techlambdas.delearmanagementapp.constant.BookingStatus;
import com.techlambdas.delearmanagementapp.constant.PaymentType;
import com.techlambdas.delearmanagementapp.model.Booking;
import com.techlambdas.delearmanagementapp.request.BookingRequest;
import com.techlambdas.delearmanagementapp.response.BookingResponse;
import com.techlambdas.delearmanagementapp.service.BookingService;
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
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest)
    {
        Booking booking=bookingService.createBooking(bookingRequest);
        return successResponse(HttpStatus.CREATED,"booking",booking);
    }
    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings(@RequestParam(required = false) String bookingNo,
                                                        @RequestParam(required = false) String customerName,
                                                        @RequestParam(required = false) PaymentType paymentType,
                                                        @RequestParam (required = false)String branchId,
                                                        @RequestParam (required = false)String branchName,
                                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                        @RequestParam(required = false)  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                                @RequestParam(required = false)BookingStatus bookingStatus)
    {
        List<BookingResponse> bookingResponses=bookingService.getAllBookings(bookingNo,customerName,paymentType,branchId,branchName,fromDate,toDate,bookingStatus);
        return successResponse(HttpStatus.OK,"bookings",bookingResponses);
    }
    @GetMapping("/page")
    public ResponseEntity<Page<BookingResponse>> getAllBookingsWithPage(@RequestParam(required = false) String bookingNo,
                                                                        @RequestParam(required = false) String customerName,
                                                                        @RequestParam(required = false) PaymentType paymentType,
                                                                        @RequestParam (required = false)String branchId,
                                                                        @RequestParam (required = false)String branchName,
                                                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                        @RequestParam(required = false)  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size,
                                                                        @RequestParam(required = false)BookingStatus bookingStatus)
    {
        Pageable pageable= PageRequest.of(page,size);
        Page<BookingResponse> bookingResponses=bookingService.getAllBookingsWithPage(bookingNo,customerName,paymentType,branchId,branchName,fromDate,toDate,pageable,bookingStatus);
        return successResponse(HttpStatus.OK,"bookingsWithPage",bookingResponses);
    }
    @GetMapping("/{bookingNo}")
    public ResponseEntity<BookingResponse> getBookingByBookingNo(@PathVariable String bookingNo)
    {
        BookingResponse booking=bookingService.getBookingByBookingNo(bookingNo);
        return successResponse(HttpStatus.OK,"booking",booking);
    }
    @PatchMapping("/cancel/{bookingNo}")
    public ResponseEntity<String> cancelBooking(@PathVariable String bookingNo)
    {
        String result=bookingService.cancelBooking(bookingNo);
        return successResponse(HttpStatus.CREATED,"success",result);
    }
    @GetMapping("/booking/{customerId}")
    public ResponseEntity<List<BookingResponse>> getBookingByCustomerId(@PathVariable String customerId)
    {
        List<BookingResponse> bookings=bookingService.getBookingsByCustomerId(customerId);
        return successResponse(HttpStatus.OK,"bookings",bookings);
    }
}
