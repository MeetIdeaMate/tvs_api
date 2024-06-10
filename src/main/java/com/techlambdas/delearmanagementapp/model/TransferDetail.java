package com.techlambdas.delearmanagementapp.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TransferDetail {
    private String transferFromBranch;
    private String transferToBranch;
    private LocalDateTime transferDate;
    private LocalDateTime receivedDate;
}
