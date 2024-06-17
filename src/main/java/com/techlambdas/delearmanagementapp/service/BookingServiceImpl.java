package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.BookingMapper;
import com.techlambdas.delearmanagementapp.mapper.CommonMapper;
import com.techlambdas.delearmanagementapp.model.Booking;
import com.techlambdas.delearmanagementapp.repository.BookingRepository;
import com.techlambdas.delearmanagementapp.repository.CustomBookingRepository;
import com.techlambdas.delearmanagementapp.request.BookingRequest;
import com.techlambdas.delearmanagementapp.response.BookingResponse;
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
public class BookingServiceImpl implements BookingService{
    @Autowired
    private BookingMapper bookingMapper;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ConfigService configService;
    @Autowired
    private CustomBookingRepository customBookingRepository;
    @Autowired
    private CommonMapper commonMapper;

    @Override
    public Booking createBooking(BookingRequest bookingRequest) {
        try {
            Booking booking=bookingMapper.mapBookingRequestToBooking(bookingRequest);
            booking.setBookingNo(configService.getNextBookingNoSequence());
            return bookingRepository.save(booking);
        }catch (Exception ex){
            throw new RuntimeException("Internal Server Error -- "+ex.getMessage(),ex.getCause());
        }
    }

    @Override
    public List<BookingResponse> getAllBookings(String bookingNo, String customerName, String paymentType, LocalDate fromDate, LocalDate toDate) {
       List<Booking> bookings=customBookingRepository.getAllBookings(bookingNo,customerName,paymentType,fromDate,toDate);
       return bookings.stream().map(commonMapper::ToBookingResponse).collect(Collectors.toList());
    }

    @Override
    public Page<BookingResponse> getAllBookingsWithPage(String bookingNo, String customerName, String paymentType, LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        Page<Booking> bookings=customBookingRepository.getAllBookingsWithPage(bookingNo,customerName,paymentType,fromDate,toDate,pageable);
        List<BookingResponse> bookingResponses=bookings.stream()
                .map(commonMapper::ToBookingResponse).collect(Collectors.toList());
        return new PageImpl<>(bookingResponses,pageable,bookings.getTotalElements());
    }

    @Override
    public Booking getBookingByBookingNo(String bookingNo) {
        Booking booking=bookingRepository.findByBookingNo(bookingNo);
        if (!Optional.ofNullable(booking).isPresent())
            throw new DataNotFoundException("Booking not found with this ID : "+bookingNo);
        return booking;
    }

    @Override
    public String cancelBooking(String bookingNo) {
        Optional<Booking> existingBooking = Optional.ofNullable(bookingRepository.findByBookingNo(bookingNo));
        if (existingBooking.isPresent())
        {
            Booking booking=existingBooking.get();
            booking.setCancelled(true);
            bookingRepository.save(booking);
            return "Cancelled successfully";
        }else {
            throw new DataNotFoundException("Booking Not found this bookingNo : "+bookingNo);
        }
    }

    @Override
    public List<Booking> getBookingsByCustomerId(String customerId) {
        List<Booking> bookings=bookingRepository.findBookingByCustomerId(customerId);
        if (!Optional.ofNullable(bookings).isPresent())
            throw new DataNotFoundException("Booking not found With this CustomerID : "+customerId);
        return bookings;
    }
}
