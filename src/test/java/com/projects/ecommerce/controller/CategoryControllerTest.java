package com.projects.ecommerce.controller;

import com.projects.ecommerce.common.ApiResponse;
import com.projects.ecommerce.dto.CategoryDto;
import com.projects.ecommerce.service.CategoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class CategoryControllerTest {

    @Mock
    private AutoCloseable closeable;

    @BeforeEach
    void initService() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    public List<CategoryDto> createDummyCategoryDtoData() {
        List<CategoryDto> categoryList = new ArrayList<>();
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryName("TestCategory");
        categoryDto.setId(1);
        categoryDto.setDescription("Test category");
        categoryDto.setImageUrl("img/url");
        categoryList.add(categoryDto);
        return categoryList;
    }

    @Test
    void createCategoryTest() {
        CategoryService categoryService = Mockito.mock(CategoryService.class);
        CategoryController categoryController = new CategoryController();
        ReflectionTestUtils.setField(categoryController, "categoryService", categoryService);
        List<CategoryDto> categoryDtoList = createDummyCategoryDtoData();
        when(categoryService.createCategory(categoryDtoList.get(0))).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        CategoryDto categoryDto = categoryDtoList.get(0);
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class, () -> categoryController.createCategory(categoryDto));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
    }

    @Test
    void listCategory() {
        CategoryService categoryService = Mockito.mock(CategoryService.class);
        CategoryController categoryController = new CategoryController();
        ReflectionTestUtils.setField(categoryController, "categoryService", categoryService);
        List<CategoryDto> categoryDtoList = createDummyCategoryDtoData();
        when(categoryService.listCategory()).thenReturn(categoryDtoList);
        List<CategoryDto> response = categoryController.listCategory();
        assertNotNull(response);
    }

    @Test
    void updateCategoryTest() {
        CategoryService categoryService = Mockito.mock(CategoryService.class);
        CategoryController categoryController = new CategoryController();
        ReflectionTestUtils.setField(categoryController, "categoryService", categoryService);
        List<CategoryDto> categoryDtoList = createDummyCategoryDtoData();
        when(categoryService.editCategory(categoryDtoList.get(0))).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        CategoryDto categoryDto = categoryDtoList.get(0);
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class, () -> categoryController.updateCategory(categoryDto));
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }

    @Test
    void deleteCategoryTest(){
        CategoryService categoryService = Mockito.mock(CategoryService.class);
        CategoryController categoryController = new CategoryController();
        ReflectionTestUtils.setField(categoryController, "categoryService", categoryService);
        List<CategoryDto> categoryDtoList = createDummyCategoryDtoData();
        doNothing().when(categoryService).deleteCategory(categoryDtoList.get(0).getId());
        ResponseEntity<ApiResponse> response = categoryController.deleteCategory(categoryDtoList.get(0).getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}