package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Booking;
import com.techlambdas.delearmanagementapp.response.BookingResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRepository extends MongoRepository<Booking,String> {
    Booking findByBookingNo(String bookingNo);

    List<Booking> findBookingByCustomerId(String customerId);
}
