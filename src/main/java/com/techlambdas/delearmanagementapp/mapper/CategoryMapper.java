package com.techlambdas.delearmanagementapp.mapper;

import com.techlambdas.delearmanagementapp.model.Category;
import com.techlambdas.delearmanagementapp.request.CategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category mapCategoryRequestToCategory(CategoryRequest categoryRequest);
    void updateCategoryrFromRequest(CategoryRequest categoryRequest, @MappingTarget Category existingCategory);
}
