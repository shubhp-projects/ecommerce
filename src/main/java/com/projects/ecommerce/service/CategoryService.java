package com.projects.ecommerce.service;

import com.projects.ecommerce.dto.CategoryDto;
import com.projects.ecommerce.exceptions.CategoryNotExistsException;
import com.projects.ecommerce.model.Category;
import com.projects.ecommerce.repository.CategoryRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    CategoryRepo categoryRepo;
    @Autowired
    ModelMapper modelMapper;

    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = convertDtoToEntity(categoryDto);
        categoryRepo.save(category);
        return convertEntityToDto(category);
    }

    public List<CategoryDto> listCategory() {
        return categoryRepo.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    public CategoryDto editCategory(CategoryDto updateCategoryDto) {
        Category category = categoryRepo.findById(updateCategoryDto.getId()).orElseThrow(() -> new CategoryNotExistsException("Category does not exists!"));
        category.setCategoryName(updateCategoryDto.getCategoryName());
        category.setDescription(updateCategoryDto.getDescription());
        category.setImageUrl(updateCategoryDto.getImageUrl());
        categoryRepo.save(category);
        return convertEntityToDto(category);
    }

    public void deleteCategory(Integer categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new CategoryNotExistsException("Category does not exists!"));
        categoryRepo.delete(category);
    }

    public CategoryDto convertEntityToDto(Category result) {
        return modelMapper.map(result, CategoryDto.class);
    }

    public Category convertDtoToEntity(CategoryDto result) {
        return modelMapper.map(result, Category.class);
    }
}
