package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.model.Booking;
import com.techlambdas.delearmanagementapp.request.BookingRequest;
import com.techlambdas.delearmanagementapp.response.BookingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    Booking createBooking(BookingRequest bookingRequest);

    List<BookingResponse> getAllBookings(String bookingNo, String customerName, String paymentType, LocalDate fromDate, LocalDate toDate);

    Page<BookingResponse> getAllBookingsWithPage(String bookingNo, String customerName, String paymentType, LocalDate fromDate, LocalDate toDate, Pageable pageable);

    Booking getBookingByBookingNo(String bookingNo);

    String cancelBooking(String bookingNo);

    List<Booking> getBookingsByCustomerId(String customerId);
}
