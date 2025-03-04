package com.example.shoppingCart.service.categore;

import com.example.shoppingCart.model.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long id);
    Category geCategoryByName(String name);
    List<Category> getAllCategory();
    Category addCategory(Category category);
    Category updateCategory(Category category , Long id);
    void deleteCategoryById(Long id);


}
