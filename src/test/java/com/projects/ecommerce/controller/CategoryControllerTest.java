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

    @Test
    void createCategory() {
        CategoryService categoryService = Mockito.mock(CategoryService.class);
        CategoryController categoryController = new CategoryController();
        ReflectionTestUtils.setField(categoryController, "categoryService", categoryService);
        List<CategoryDto> categoryList = createDummyData();
        Mockito.when(categoryService.createCategory(Mockito.any(CategoryDto.class))).thenReturn(categoryList.get(0));
        ResponseEntity<CategoryDto> response = categoryController.createCategory(categoryList.get(0));
        assertEquals(1, categoryList.get(0).getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createCategoryException() {
        CategoryService categoryService = Mockito.mock(CategoryService.class);
        CategoryController categoryController = new CategoryController();
        ReflectionTestUtils.setField(categoryController, "categoryService", categoryService);
        List<CategoryDto> categoryList = createDummyData();
        Mockito.when(categoryService.createCategory(Mockito.any(CategoryDto.class))).thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        CategoryDto categoryDto = categoryList.get(0);
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class, () -> {
            categoryController.createCategory(categoryDto);
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getStatus());
    }

    @Test
    void listCategory() {
        CategoryService categoryService = Mockito.mock(CategoryService.class);
        CategoryController categoryController = new CategoryController();
        ReflectionTestUtils.setField(categoryController, "categoryService", categoryService);
        List<CategoryDto> categoryList = createDummyData();
        Mockito.when(categoryService.listCategory()).thenReturn(categoryList);
        List<CategoryDto> response = categoryController.listCategory();
        assertNotNull(response);
    }

    @Test
    void updateCategory() {
        CategoryService categoryService = Mockito.mock(CategoryService.class);
        CategoryController categoryController = new CategoryController();
        ReflectionTestUtils.setField(categoryController, "categoryService", categoryService);
        List<CategoryDto> categoryList = createDummyData();
        Mockito.doNothing().when(categoryService).editCategory(Mockito.any(CategoryDto.class));
        ResponseEntity<ApiResponse> response = categoryController.updateCategory(categoryList.get(0));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteCategory(){
        CategoryService categoryService = Mockito.mock(CategoryService.class);
        CategoryController categoryController = new CategoryController();
        ReflectionTestUtils.setField(categoryController, "categoryService", categoryService);
        List<CategoryDto> categoryList = createDummyData();
        Mockito.doNothing().when(categoryService).deleteCategory(Mockito.any(CategoryDto.class));
        ResponseEntity<ApiResponse> response = categoryController.deleteCategory(categoryList.get(0));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}