package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CustomBookingRepository {
    List<Booking> getAllBookings(String bookingNo, String customerName, String paymentType,String branchId,String branchName,LocalDate fromDate, LocalDate toDate);

    Page<Booking> getAllBookingsWithPage(String bookingNo, String customerName, String paymentType,String branchId,String branchName, LocalDate fromDate, LocalDate toDate, Pageable pageable);
}
