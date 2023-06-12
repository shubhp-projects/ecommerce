package com.projects.ecommerce.service;

import com.projects.ecommerce.dto.CategoryDto;
import com.projects.ecommerce.model.Category;
import com.projects.ecommerce.repository.CategoryRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CategoryServiceTest {

    public List<CategoryDto> createDummyData() {
        List<CategoryDto> categoryList = new ArrayList<>();
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryName("TestCategory");
        categoryDto.setId(1);
        categoryDto.setDescription("Test category");
        categoryDto.setImageUrl("img/url");
        categoryList.add(categoryDto);
        return categoryList;
    }

    public List<Category> createDummyCategoryData() {
        List<Category> categoryList = new ArrayList<>();
        Category category = new Category();
        category.setCategoryName("TestCategory");
        category.setId(1);
        category.setDescription("Test category");
        category.setImageUrl("img/url");
        categoryList.add(category);
        return categoryList;
    }

    @Test
    void createCategory() {
        CategoryRepo categoryRepo = Mockito.mock(CategoryRepo.class);
        ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
        CategoryService categoryService = new CategoryService();
        ReflectionTestUtils.setField(categoryService, "categoryRepo", categoryRepo);
        ReflectionTestUtils.setField(categoryService, "modelMapper", modelMapper);
        List<Category> categoryList = createDummyCategoryData();
        List<CategoryDto> categoryDtoList = createDummyData();
        Mockito.when(categoryRepo.save(Mockito.any(Category.class))).thenReturn(categoryList.get(0));
        categoryService.createCategory(categoryDtoList.get(0));
        assertNotNull(categoryList.get(0).getCategoryName());
    }

    @Test
    void listCategory() {
        CategoryRepo categoryRepo = Mockito.mock(CategoryRepo.class);
        ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
        CategoryService categoryService = new CategoryService();
        ReflectionTestUtils.setField(categoryService, "categoryRepo", categoryRepo);
        ReflectionTestUtils.setField(categoryService, "modelMapper", modelMapper);
        List<Category> categoryList = createDummyCategoryData();
        List<CategoryDto> categoryDtoList = createDummyData();
        Mockito.when(categoryRepo.findAll()).thenReturn(categoryList);
        categoryService.listCategory();
        assertNotNull(categoryList.get(0).getCategoryName());
    }

    @Test
    void editCategoryTest() {
        CategoryRepo categoryRepo = Mockito.mock(CategoryRepo.class);
        ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
        CategoryService categoryService = new CategoryService();
        ReflectionTestUtils.setField(categoryService, "categoryRepo", categoryRepo);
        ReflectionTestUtils.setField(categoryService, "modelMapper", modelMapper);
        List<Category> categoryList = createDummyCategoryData();
        List<CategoryDto> categoryDtoList = createDummyData();
        Mockito.when(categoryRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(categoryList.get(0)));
        Mockito.when(categoryRepo.save(Mockito.any(Category.class))).thenReturn(categoryList.get(0));
        categoryService.editCategory(categoryDtoList.get(0));
        assertNotNull(categoryList.get(0).getCategoryName());
    }

    @Test
    void deleteCategoryTest() {
        CategoryRepo categoryRepo = Mockito.mock(CategoryRepo.class);
        ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
        CategoryService categoryService = new CategoryService();
        ReflectionTestUtils.setField(categoryService, "categoryRepo", categoryRepo);
        ReflectionTestUtils.setField(categoryService, "modelMapper", modelMapper);
        List<Category> categoryList = createDummyCategoryData();
        List<CategoryDto> categoryDtoList = createDummyData();
        Mockito.when(categoryRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(categoryList.get(0)));
        categoryService.deleteCategory(categoryDtoList.get(0));
        assertNotNull(categoryList.get(0).getCategoryName());
    }
}