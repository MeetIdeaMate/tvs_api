package com.techlambdas.delearmanagementapp.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TransferDetail {
    private LocalDate transferDate;
    private String transferFromBranch;
    private String transferToBranch;
}
