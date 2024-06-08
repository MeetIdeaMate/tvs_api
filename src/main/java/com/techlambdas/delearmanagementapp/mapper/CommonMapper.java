package com.techlambdas.delearmanagementapp.mapper;

import com.techlambdas.delearmanagementapp.model.*;
import com.techlambdas.delearmanagementapp.repository.*;
import com.techlambdas.delearmanagementapp.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class CommonMapper {
    @Autowired
    protected BranchRepository branchRepository;
    @Autowired
    protected VendorRepository vendorRepository;
    @Autowired
    protected CategoryRepository categoryRepository;
    @Autowired
    protected ItemRepository itemRepository;
    @Autowired
    protected CustomerRepository customerRepository;

    @Mapping(target = "branchName", source = "branchId", qualifiedByName = "mapBranchName")
    @Mapping(target = "vendorName", source = "vendorId", qualifiedByName= "mapVendorName")
    public abstract PurchaseResponse toPurchaseResponse(Purchase purchase) ;

    @Mapping(target = "categoryName", source = "categoryId", qualifiedByName = "mapCategoryName")
    @Mapping(target = "hsnSacCode", source = "categoryId", qualifiedByName = "mapHsnSacCode")
    @Mapping(target = "itemName", source = "partNo", qualifiedByName = "mapItemName")
    public abstract ItemDetailResponse toItemDetailResponse(ItemDetail itemDetail);


    @Named("mapBranchName")
    public String mapBranchName(String branchId) {
        return branchRepository.getBranchName(branchId);
    }
    @Named("mapVendorName")
    public String mapVendorName(String vendorId) {
        return vendorRepository.getVendorName(vendorId);
    }
    @Named("mapCategoryName")
    public String mapCategoryName(String categoryId) {
        return categoryRepository.getCategoryName(categoryId);
    }
    @Named("mapHsnSacCode")
    public String mapHsnSacCode(String categoryId) {
        return categoryRepository.gethsnSacCode(categoryId);
    }
    @Named("mapItemName")
    public String mapItemName(String partNo) {
        return itemRepository.getItemName(partNo);
    }

    public ItemDetailsWithPartNoResponse toItemDetailsWithPartNoResponse(ItemDetail itemDetail) {
        // Implement the mapping logic here
        ItemDetailsWithPartNoResponse response = new ItemDetailsWithPartNoResponse();
        response.setPartNo(itemDetail.getPartNo());
        response.setCategoryId(itemDetail.getCategoryId());
        response.setSpecificationsValue(itemDetail.getSpecificationsValue());
        response.setUnitRate(itemDetail.getUnitRate());
        response.setQuantity(itemDetail.getQuantity());
        response.setValue(itemDetail.getValue());
        response.setDiscount(itemDetail.getDiscount());
        response.setTaxableValue(itemDetail.getTaxableValue());
        response.setInvoiceValue(itemDetail.getInvoiceValue());
        response.setFinalInvoiceValue(itemDetail.getFinalInvoiceValue());
        return response;
    }

    @Mapping(target = "customerName", source = "customerId", qualifiedByName= "mapCustomerName")
    @Mapping(target = "mobileNo", source = "customerId", qualifiedByName= "mapMobileNo")
    public abstract  SalesResponse toSalesResponse (Sales sales) ;

    @Named("mapCustomerName")
    public String mapCustomerName(String customerId) {
        return customerRepository.getCustomerName(customerId);
    }
@Named("mapMobileNo")
    public String mapMobileNo(String customerId){
        return customerRepository.getMobileNo(customerId);
    }


}
