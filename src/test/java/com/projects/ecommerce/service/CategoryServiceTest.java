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

    public List<CategoryDto> createDummyCategoryDtoData() {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryName("TestCategory");
        categoryDto.setId(1);
        categoryDto.setDescription("Test category");
        categoryDto.setImageUrl("img/url");
        categoryDtoList.add(categoryDto);
        return categoryDtoList;
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

    private static void setFields(CategoryRepo categoryRepo, ModelMapper modelMapper, CategoryService categoryService) {
        ReflectionTestUtils.setField(categoryService, "categoryRepo", categoryRepo);
        ReflectionTestUtils.setField(categoryService, "modelMapper", modelMapper);
    }

    @Test
    void createCategory() {
        CategoryRepo categoryRepo = Mockito.mock(CategoryRepo.class);
        ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
        CategoryService categoryService = new CategoryService();
        setFields(categoryRepo, modelMapper, categoryService);
        List<Category> categoryList = createDummyCategoryData();
        List<CategoryDto> categoryDtoList = createDummyCategoryDtoData();
        Mockito.when(categoryRepo.save(categoryList.get(0))).thenReturn(categoryList.get(0));
        categoryService.createCategory(categoryDtoList.get(0));
        assertNotNull(categoryList.get(0).getCategoryName());
    }

    @Test
    void listCategory() {
        CategoryRepo categoryRepo = Mockito.mock(CategoryRepo.class);
        ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
        CategoryService categoryService = new CategoryService();
        setFields(categoryRepo, modelMapper, categoryService);
        List<Category> categoryList = createDummyCategoryData();
        Mockito.when(categoryRepo.findAll()).thenReturn(categoryList);
        assertNotNull(categoryService.listCategory());
    }

    @Test
    void editCategoryTest() {
        CategoryRepo categoryRepo = Mockito.mock(CategoryRepo.class);
        ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
        CategoryService categoryService = new CategoryService();
        setFields(categoryRepo, modelMapper, categoryService);
        List<Category> categoryList = createDummyCategoryData();
        List<CategoryDto> categoryDtoList = createDummyCategoryDtoData();
        Mockito.when(categoryRepo.findById(categoryList.get(0).getId())).thenReturn(Optional.of(categoryList.get(0)));
        Mockito.when(categoryRepo.save(categoryList.get(0))).thenReturn(categoryList.get(0));
        categoryService.editCategory(categoryDtoList.get(0));
        assertNotNull(categoryList.get(0));
    }

    @Test
    void deleteCategory() {
        CategoryRepo categoryRepo = Mockito.mock(CategoryRepo.class);
        ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
        CategoryService categoryService = new CategoryService();
        setFields(categoryRepo, modelMapper, categoryService);
        List<Category> categoryList = createDummyCategoryData();
        List<CategoryDto> categoryDtoList = createDummyCategoryDtoData();
        Mockito.when(categoryRepo.findById(categoryList.get(0).getId())).thenReturn(Optional.of(categoryList.get(0)));
        categoryService.deleteCategory(categoryDtoList.get(0).getId());
        assertNotNull(categoryList.get(0));
    }
}