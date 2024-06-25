package com.techlambdas.delearmanagementapp.request;

import lombok.Data;

@Data
public class ItemRequest
{
    private String partNo;
    private String itemName;
    private String categoryId;
    private boolean isTaxable;
    private boolean isIncentive;
    private String hsnSacCode;
}
