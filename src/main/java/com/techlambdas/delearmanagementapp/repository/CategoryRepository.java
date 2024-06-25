package com.techlambdas.delearmanagementapp.repository;

import com.techlambdas.delearmanagementapp.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
    Category findBycategoryId(String categoryId);

    default String getCategoryName(String categoryId)
    {
        Category category= findBycategoryId(categoryId);
        return (category !=null)?category.getCategoryName():"unknown Category";
    }
    default String getCategoryNameByPartNo(String categoryId)
    {
        Category category=findBycategoryId(categoryId);
        return (category!=null)?category.getCategoryName():"unknown Category";
    }
}
