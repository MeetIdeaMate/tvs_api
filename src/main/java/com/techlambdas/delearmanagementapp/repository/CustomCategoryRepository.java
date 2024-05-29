package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomCategoryRepository {
    List<Category> getAllCategorys(String categoryId, String categoryName);

    Page<Category> getAllCaregorysWithPage(String categoryId, String categoryName, Pageable pageable);
}
