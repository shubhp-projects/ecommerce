package com.projects.ecommerce.service;

import com.projects.ecommerce.dto.ProductDto;
import com.projects.ecommerce.exceptions.ProductNotExistsException;
import com.projects.ecommerce.model.Category;
import com.projects.ecommerce.model.Product;
import com.projects.ecommerce.repository.ProductRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    private static void setFields(ProductRepo productRepo, ModelMapper modelMapper, ProductService productService) {
        ReflectionTestUtils.setField(productService, "productRepo", productRepo);
        ReflectionTestUtils.setField(productService, "modelMapper", modelMapper);
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

    public List<Product> createDummyProductData() {
        List<Product> productList = new ArrayList<>();
        Product product = new Product();
        product.setCategory(createDummyCategoryData().get(0));
        product.setName("TestProduct");
        product.setDescription("Test product");
        product.setId(1);
        product.setImageUrl("img/url");
        productList.add(product);
        return productList;
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
    void createProductTest() {
        ProductRepo productRepo = Mockito.mock(ProductRepo.class);
        ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
        ProductService productService = new ProductService();
        setFields(productRepo, modelMapper, productService);
        List<Product> productList = createDummyProductData();
        List<ProductDto> productDtoList = createDummyProductDtoData();
        when(productRepo.save(productList.get(0))).thenReturn(productList.get(0));
        productService.createProduct(productDtoList.get(0));
        assertNotNull(productList.get(0));
    }

    @Test
    void getAllProductsTest() {
        ProductRepo productRepo = Mockito.mock(ProductRepo.class);
        ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
        ProductService productService = new ProductService();
        setFields(productRepo, modelMapper, productService);
        List<Product> productList = createDummyProductData();
        when(productRepo.findAll()).thenReturn(productList);
        assertNotNull(productService.getAllProducts());
    }

    @Test
    void updateProductTest() {
        ProductRepo productRepo = Mockito.mock(ProductRepo.class);
        ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
        ProductService productService = new ProductService();
        setFields(productRepo, modelMapper, productService);
        List<Product> productList = createDummyProductData();
        List<ProductDto> productDtoList = createDummyProductDtoData();
        when(productRepo.findById(productList.get(0).getId())).thenReturn(Optional.of(productList.get(0)));
        when(productRepo.save(productList.get(0))).thenReturn(productList.get(0));
        productService.updateProduct(productDtoList.get(0));
        assertNotNull(productList.get(0));
    }

    @Test
    void deleteProductTest() {
        ProductRepo productRepo = Mockito.mock(ProductRepo.class);
        ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
        ProductService productService = new ProductService();
        setFields(productRepo, modelMapper, productService);
        List<Product> productList = createDummyProductData();
        List<ProductDto> productDtoList = createDummyProductDtoData();
        when(productRepo.findById(productList.get(0).getId())).thenReturn(Optional.ofNullable(productList.get(0)));
        productService.deleteProduct(productDtoList.get(0).getId());
        assertNotNull(productList.get(0));
    }

    @Test
    void findByIdTest() {
        ProductRepo productRepo = Mockito.mock(ProductRepo.class);
        ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
        ProductService productService = new ProductService();
        setFields(productRepo, modelMapper, productService);
        List<Product> productList = createDummyProductData();
        when(productRepo.findById(productList.get(0).getId())).thenReturn(Optional.of(productList.get(0)));
        productService.findById(productList.get(0).getId());
        assertNotNull(productList.get(0));
    }

    @Test
    void findByIdWhenProductIsNotPresentTest() {
        ProductRepo productRepo = Mockito.mock(ProductRepo.class);
        ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
        ProductService productService = new ProductService();
        setFields(productRepo, modelMapper, productService);
        List<Product> productList = createDummyProductData();
        Integer productId = productList.get(0).getId();
        when(productRepo.findById(productId)).thenReturn(Optional.empty());
        ProductNotExistsException thrown = assertThrows(ProductNotExistsException.class, () -> productService.findById(productId));
        assertEquals("Product ID is invalid: 1", thrown.getMessage());
    }
}