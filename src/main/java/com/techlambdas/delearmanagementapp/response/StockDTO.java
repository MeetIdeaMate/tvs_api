package com.techlambdas.delearmanagementapp.response;
import com.techlambdas.delearmanagementapp.constant.StockStatus;
import lombok.Data;
import java.util.List;


@Data
public class StockDTO {
    private String partNo;
    private String itemName;
    private String categoryId;
    private String categoryName;
    private List<StockItem> stockItems;
    private int totalQuantity;
    private String branchId;
    private String branchName;
    private String hsnSacCode;
    private StockStatus stockStatus;
}
