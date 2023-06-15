package com.projects.ecommerce.controller;

import com.projects.ecommerce.common.ApiResponse;
import com.projects.ecommerce.dto.ProductDto;
import com.projects.ecommerce.exceptions.CategoryNotExistsException;
import com.projects.ecommerce.exceptions.ProductNotExistsException;
import com.projects.ecommerce.repository.CategoryRepo;
import com.projects.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    CategoryRepo categoryRepo;

    @PostMapping("/add")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        categoryRepo.findById(productDto.getCategoryId()).orElseThrow(() -> new CategoryNotExistsException("Category does not exists!"));
        return ResponseEntity.ok(productService.createProduct(productDto));
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto) throws ProductNotExistsException {
        try {
            return ResponseEntity.ok(productService.updateProduct(productDto));
        } catch (ResponseStatusException responseStatusException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("productId") Integer productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(new ApiResponse(true, "Product has been removed!"), HttpStatus.OK);
    }
}
