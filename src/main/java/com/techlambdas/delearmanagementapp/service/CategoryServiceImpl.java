package com.techlambdas.delearmanagementapp.service;

import com.techlambdas.delearmanagementapp.exception.DataNotFoundException;
import com.techlambdas.delearmanagementapp.mapper.CategoryMapper;
import com.techlambdas.delearmanagementapp.model.Category;
import com.techlambdas.delearmanagementapp.model.Vendor;
import com.techlambdas.delearmanagementapp.repository.CategoryRepository;
import com.techlambdas.delearmanagementapp.repository.CustomCategoryRepository;
import com.techlambdas.delearmanagementapp.request.CategoryRequest;
import com.techlambdas.delearmanagementapp.request.VendorRequest;
import com.techlambdas.delearmanagementapp.utils.RandomIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CustomCategoryRepository customCategoryRepository;

    @Override
    public Category createCategory(CategoryRequest categoryRequest) {
        try {
            Category category = categoryMapper.mapCategoryRequestToCategory(categoryRequest);
            category.setCategoryId(RandomIdGenerator.getRandomId());
            return categoryRepository.save(category);
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }
    @Override
    public List<Category> getAllCategorys(String categoryId, String categoryName) {
        List<Category> categorys=customCategoryRepository.getAllCategorys(categoryId,categoryName);
        return categorys;
    }

    @Override
    public Category updateCategory(String categoryId, CategoryRequest categoryRequest) {
        try {
            Category existingCategory = categoryRepository.findBycategoryId(categoryId);
            if (existingCategory == null)
                throw new DataNotFoundException("vendor not found with ID: " + categoryId);
            categoryMapper.updateCategoryrFromRequest(categoryRequest, existingCategory);
            return categoryRepository.save(existingCategory);
        }catch (DataNotFoundException ex) {
            throw new DataNotFoundException("Data not found --" + ex.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Internal Server Error --" + ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public Page<Category> getAllCategorysWithPage(String categoryId, String categoryName, Pageable pageable) {
        return customCategoryRepository.getAllCaregorysWithPage(categoryId,categoryName,  pageable);
    }
}
