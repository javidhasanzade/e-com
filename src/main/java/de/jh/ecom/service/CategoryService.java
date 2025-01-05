package de.jh.ecom.service;

import de.jh.ecom.model.Category;
import de.jh.ecom.payload.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategories();
    void createCategory(Category category);
    String deleteCategory(Long id);
    Category updateCategory(Category category, Long categoryId);
}
