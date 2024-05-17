package com.techlambdas.delearmanagementapp.model;

import com.techlambdas.delearmanagementapp.model.submodel.AccessoriesDetail;
import com.techlambdas.delearmanagementapp.model.submodel.VehicleDetail;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "purchases")
public class Purchase {
    @Id
    private String id;
    private String purchaseId;
    private String purchaseType;
    private String invoiceNo;
    private LocalDate invoiceDate;
    private String purchaseRefNo;
    private boolean isIgst;
    private String carrier;
    private String carrierNumber;
    private List<VehicleDetail> vehicleDetails;
    private List<AccessoriesDetail>accessoriesDetails;
    @CreatedDate
    private LocalDateTime createdDateTime;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updatedDateTime;
    @LastModifiedBy
    private String updatedBy;
}
