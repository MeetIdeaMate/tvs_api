package com.techlambdas.delearmanagementapp.mapper;

import com.techlambdas.delearmanagementapp.model.*;
import com.techlambdas.delearmanagementapp.repository.BranchRepository;
import com.techlambdas.delearmanagementapp.repository.CategoryRepository;
import com.techlambdas.delearmanagementapp.repository.ItemRepository;
import com.techlambdas.delearmanagementapp.repository.VendorRepository;
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

    @Mapping(target = "categoryName", source = "categoryId", qualifiedByName = "mapCategoryName")
    @Mapping(target = "hsnSacCode", source = "categoryId", qualifiedByName = "mapHsnSacCode")
    @Mapping(target = "itemName", source = "partNo", qualifiedByName = "mapItemName")
    @Mapping(target = "branchName", source = "branchId", qualifiedByName = "mapBranchName")
    public abstract StockResponse toStockResponse(Stock stock);
}
