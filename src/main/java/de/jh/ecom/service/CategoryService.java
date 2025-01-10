package de.jh.ecom.service;

import de.jh.ecom.payload.CategoryDto;
import de.jh.ecom.payload.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize);
    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto deleteCategory(Long id);
    CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId);
}
