package com.techlambdas.delearmanagementapp.model;

import com.techlambdas.delearmanagementapp.constant.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransferDetail {
    private String transferId;
    private String transferFromBranch;
    private String transferToBranch;
    private LocalDateTime transferDate;
    private LocalDateTime receivedDate;
    private Status status;
}
