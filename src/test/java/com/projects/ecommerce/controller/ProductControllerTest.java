package com.projects.ecommerce.controller;

import com.projects.ecommerce.common.ApiResponse;
import com.projects.ecommerce.dto.ProductDto;
import com.projects.ecommerce.model.Category;
import com.projects.ecommerce.repository.CategoryRepo;
import com.projects.ecommerce.service.ProductService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductControllerTest {

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

    public List<ProductDto> createDummyProductDtoData() {
        List<ProductDto> productDtoList = new ArrayList<>();
        ProductDto productDto = new ProductDto();
        productDto.setCategoryId(createDummyCategoryData().get(0).getId());
        productDto.setName("TestProduct");
        productDto.setDescription("Test product");
        productDto.setId(1);
        productDto.setImageUrl("img/url");
        productDtoList.add(productDto);
        return productDtoList;
    }

    @Test
    void createProduct() {
        ProductService productService = Mockito.mock(ProductService.class);
        CategoryRepo categoryRepo = Mockito.mock(CategoryRepo.class);
        ProductController productController = new ProductController();
        ReflectionTestUtils.setField(productController, "productService", productService);
        ReflectionTestUtils.setField(productController, "categoryRepo", categoryRepo);
        List<Category> categoryList = createDummyCategoryData();
        List<ProductDto> productDtoList = createDummyProductDtoData();
        Mockito.when(categoryRepo.findById(productDtoList.get(0).getCategoryId())).thenReturn(Optional.of(categoryList.get(0)));
        productController.createProduct(productDtoList.get(0));
        assertEquals(1, productDtoList.get(0).getId());
    }

    @Test
    void getProducts() {
        ProductService productService = Mockito.mock(ProductService.class);
        ProductController productController = new ProductController();
        ReflectionTestUtils.setField(productController, "productService", productService);
        List<ProductDto> productDtoList = createDummyProductDtoData();
        Mockito.when(productService.getAllProducts()).thenReturn(productDtoList);
        ResponseEntity<List<ProductDto>> response = productController.getProducts();
        assertNotNull(response);
    }

    @Test
    void updateProduct() {
        ProductService productService = Mockito.mock(ProductService.class);
        ProductController productController = new ProductController();
        ReflectionTestUtils.setField(productController, "productService", productService);
        List<ProductDto> productDtoList = createDummyProductDtoData();
        Mockito.when(productService.updateProduct(productDtoList.get(0))).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        ProductDto productDto = productDtoList.get(0);
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class, () -> {
            productController.updateProduct(productDto);
        });
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }

    @Test
    void deleteProduct() {
        ProductService productService = Mockito.mock(ProductService.class);
        ProductController productController = new ProductController();
        ReflectionTestUtils.setField(productController, "productService", productService);
        List<ProductDto> productDtoList = createDummyProductDtoData();
        Mockito.doNothing().when(productService).deleteProduct(productDtoList.get(0).getId());
        ResponseEntity<ApiResponse> response = productController.deleteProduct(productDtoList.get(0).getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}