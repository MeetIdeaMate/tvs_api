package com.techlambdas.delearmanagementapp.service;


import com.techlambdas.delearmanagementapp.model.Category;
import com.techlambdas.delearmanagementapp.model.Vendor;
import com.techlambdas.delearmanagementapp.request.CategoryRequest;
import com.techlambdas.delearmanagementapp.request.VendorRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    Category createCategory(CategoryRequest categoryRequest);
    List<Category> getAllCategorys(String categoryId, String categoryName);
    Category updateCategory(String categoryId, CategoryRequest categoryRequest);
    Page<Category> getAllCategorysWithPage(String categoryId, String categoryName, Pageable pageable);
}
