package com.techlambdas.delearmanagementapp.response;
import com.techlambdas.delearmanagementapp.constant.TransferStatus;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TransferResponse {
    private List<TransferItem>transferItems;
    private int totalQuantity;
    private String transferFromBranch;
    private String fromBranchName;
    private String transferToBranch;
    private LocalDateTime transferDate;
    private LocalDateTime receivedDate;
    private TransferStatus transferStatus;
}
