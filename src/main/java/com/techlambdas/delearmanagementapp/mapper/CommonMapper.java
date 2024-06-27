package com.techlambdas.delearmanagementapp.mapper;

import com.techlambdas.delearmanagementapp.response.*;
import com.techlambdas.delearmanagementapp.model.*;
import com.techlambdas.delearmanagementapp.repository.*;
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
    @Autowired
    protected EmployeeRepository employeeRepository;

    @Mapping(target = "branchName", source = "branchId", qualifiedByName = "mapBranchName")
    @Mapping(target = "vendorName", source = "vendorId", qualifiedByName= "mapVendorName")
    public abstract PurchaseResponse toPurchaseResponse(Purchase purchase) ;

    @Mapping(target = "categoryName", source = "categoryId", qualifiedByName = "mapCategoryName")
    @Mapping(target = "hsnSacCode", source = "partNo", qualifiedByName = "mapHsnSacCode")
    @Mapping(target = "itemName", source = "partNo", qualifiedByName = "mapItemName")
    @Mapping(target = "categoryId", source = "partNo", qualifiedByName = "mapCategoryId")
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
    public String mapHsnSacCode(String partNo) {
        return itemRepository.gethsnSacCode(partNo);
    }
    @Named("mapItemName")
    public String mapItemName(String partNo) {
        return itemRepository.getItemName(partNo);
    }
    @Named("mapCategoryId")
    public String mapCategoryId(String partNo) {
        return itemRepository.getCategoryId(partNo);
    }

//    @Mapping(target = "categoryName", source = "categoryId", qualifiedByName = "mapCategoryName")
    @Mapping(target = "customerName", source = "customerId", qualifiedByName= "mapCustomerName")
    @Mapping(target = "mobileNo", source = "customerId", qualifiedByName= "mapMobileNo")
    public abstract SalesResponse toSalesResponse(Sales sales);

    @Named("mapCustomerName")
    public String mapCustomerName(String customerId) {
        return customerRepository.getCustomerName(customerId);
    }

    @Named("mapMobileNo")
    public String mapMobileNo(String customerId){
        return customerRepository.getMobileNo(customerId);
    }

    @Named("mapAddress")
    public String mapAddress(String customerId){
        return customerRepository.getAddress(customerId);
    }

    @Mapping(target = "categoryName", source = "categoryId", qualifiedByName = "mapCategoryName")
    @Mapping(target = "hsnSacCode", source = "partNo", qualifiedByName = "mapHsnSacCode")
    @Mapping(target = "itemName", source = "partNo", qualifiedByName = "mapItemName")
    @Mapping(target = "branchName", source = "branchId", qualifiedByName = "mapBranchName")
    public abstract StockResponse toStockResponse(Stock stock);

    @Mapping(target = "categoryName", source = "categoryId", qualifiedByName = "mapCategoryName")
    @Mapping(target = "itemName", source = "partNo", qualifiedByName = "mapItemName")
    public abstract TransferItem mapTransferItem(TransferItem transferItem) ;

    @Mapping(target = "itemName", source = "partNo", qualifiedByName = "mapItemName")
    @Mapping(target = "customerName", source = "customerId", qualifiedByName= "mapCustomerName")
    public abstract ReceiptResponse ToReceiptResponse(Receipt receipt);

    @Mapping(target = "itemName", source = "partNo", qualifiedByName = "mapItemName")
    @Mapping(target = "categoryId", source = "partNo", qualifiedByName = "mapCategoryId")
    @Mapping(target = "categoryName", source = "partNo", qualifiedByName = "mapCategoryNameByPartNo")
    @Mapping(target = "customerName", source = "customerId", qualifiedByName= "mapCustomerName")
    @Mapping(target = "mobileNo", source = "customerId", qualifiedByName= "mapMobileNo")
    @Mapping(target = "address", source = "customerId", qualifiedByName= "mapAddress")
    @Mapping(target = "executiveName", source = "executiveId", qualifiedByName= "mapEmployeeName")
    public abstract BookingResponse ToBookingResponse(Booking booking);

    @Named("mapCategoryNameByPartNo")
    public String mapCategoryNameByPartNo(String partNo) {
        Item item=itemRepository.findByPartNo(partNo);
        String categoryId=item.getCategoryId();
        return categoryRepository.getCategoryNameByPartNo(categoryId);
    }

    @Named("mapEmployeeName")
    public String mapEmployeeName(String employeeId) {
        return employeeRepository.getEmployeeName(employeeId);
    }

    @Mapping(target = "branchName", source = "branchId", qualifiedByName = "mapBranchName")
    public abstract UserResponse mapToUserResponse(User user);
}
