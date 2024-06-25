package com.techlambdas.delearmanagementapp.controller;

import com.techlambdas.delearmanagementapp.model.Category;
import com.techlambdas.delearmanagementapp.request.CategoryRequest;
import com.techlambdas.delearmanagementapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.techlambdas.delearmanagementapp.response.AppResponse.successResponse;

@RestController
@RequestMapping("/category")
public class CategoryController
{
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public ResponseEntity createCategory(@RequestBody CategoryRequest categoryRequest) {
        Category category = categoryService.createCategory(categoryRequest);
        return successResponse(HttpStatus.CREATED,"category",category);
    }
    @GetMapping
    public ResponseEntity<List<Category>>getAllCategory(@RequestParam(required = false) String categoryId,
                                                     @RequestParam(required = false) String categoryName){
        List<Category> categorys=categoryService.getAllCategorys(categoryId,categoryName);
        return successResponse(HttpStatus.OK,"categoryList",categorys);
    }
    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable String categoryId,@RequestBody CategoryRequest categoryRequest){
        Category category=categoryService.updateCategory(categoryId,categoryRequest);
        return successResponse(HttpStatus.OK,"category",category);
    }
    @GetMapping("/page")
    public ResponseEntity<Page<Category>> getAllCategoryWithPage(
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categorysPage = categoryService.getAllCategorysWithPage(categoryId, categoryName, pageable);

        return successResponse(HttpStatus.OK,"categorysWithPage",categorysPage);
    }
}
