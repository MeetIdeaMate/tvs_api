package com.techlambdas.delearmanagementapp.response;
import com.techlambdas.delearmanagementapp.constant.TransferStatus;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TransferResponse {
    private String transferId;
    private String fromBranchId;
    private String fromBranchName;
    private String toBranchId;
    private String toBranchName;
    private List<TransferItem>transferItems;
    private int totalQuantity;
    private LocalDateTime transferDate;
    private LocalDateTime receivedDate;
    private TransferStatus transferStatus;
}
