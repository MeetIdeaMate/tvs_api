package com.techlambdas.delearmanagementapp.request;

import com.techlambdas.delearmanagementapp.model.GstDetail;
import com.techlambdas.delearmanagementapp.model.Incentive;
import com.techlambdas.delearmanagementapp.model.MainSpecInfo;
import com.techlambdas.delearmanagementapp.model.Taxes;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ItemDetailRequest
{
    private String partNo;
    private String itemName;
    private String categoryId;
    private List<MainSpecInfo> mainSpecInfos;
    private Map<String,String> specificationsValue;
    private double unitRate;
    private int quantity;
    private double discount;
    private List<GstDetail> gstDetails;
    private List<Taxes> taxes;
    private List<Incentive>incentives;
}
