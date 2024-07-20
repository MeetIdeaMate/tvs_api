package com.techlambdas.delearmanagementapp.model;

import com.techlambdas.delearmanagementapp.constant.BookingStatus;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;
    private String bookingNo;
    private LocalDate bookingDate;
    private String customerId;
    private String branchId;
    private String partNo;
    private String additionalInfo;
    private PaidDetail paidDetail;
    private String executiveId;  //employeeId
    private LocalDate targetInvoiceDate;
    private BookingStatus bookingStatus;
    @CreatedDate
    private LocalDateTime createdDateTime;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedDateTime;
    @LastModifiedBy
    private String updatedBy;
}