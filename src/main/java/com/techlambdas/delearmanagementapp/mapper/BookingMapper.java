package com.techlambdas.delearmanagementapp.mapper;

import com.techlambdas.delearmanagementapp.model.Booking;
import com.techlambdas.delearmanagementapp.request.BookingRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface BookingMapper {
    Booking mapBookingRequestToBooking(BookingRequest bookingRequest);
}
