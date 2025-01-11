package de.jh.ecom.service;

import de.jh.ecom.exceptions.APIException;
import de.jh.ecom.exceptions.ResourceNotFoundException;
import de.jh.ecom.model.Category;
import de.jh.ecom.payload.CategoryDto;
import de.jh.ecom.payload.CategoryResponse;
import de.jh.ecom.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories = categoryPage.getContent();
        if (categories.isEmpty())
            throw new APIException("No category created till now.");

        List<CategoryDto> categoryDtos = categories.stream().map((category) -> modelMapper.map(category, CategoryDto.class)).toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDtos);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());

        return categoryResponse;
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category savedCategoryFromDb = categoryRepository.findByCategoryName(categoryDto.getCategoryName());
        if (savedCategoryFromDb != null) {
            throw new APIException("Category already exists");
        }
        Category newCategory = modelMapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(newCategory);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "caregoryId", categoryId));

        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "caregoryId", categoryId));

        Category category = modelMapper.map(categoryDto, Category.class);

        categoryDto.setCategoryId(categoryId);

        savedCategory = categoryRepository.save(category);

        return modelMapper.map(savedCategory, CategoryDto.class);
    }
}
