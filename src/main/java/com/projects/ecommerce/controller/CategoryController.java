package com.projects.ecommerce.controller;

import com.projects.ecommerce.common.ApiResponse;
import com.projects.ecommerce.dto.CategoryDto;
import com.projects.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        try {
            return ResponseEntity.ok(categoryService.createCategory(categoryDto));
        } catch (ResponseStatusException responseStatusException) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    public List<CategoryDto> listCategory() {
        return categoryService.listCategory();
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse> updateCategory(@RequestBody CategoryDto categoryDto) {
        categoryService.editCategory(categoryDto);
        return new ResponseEntity<>(new ApiResponse(true, "Category has been updated successfully!"), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@RequestBody CategoryDto categoryDto) {
        categoryService.deleteCategory(categoryDto);
        return new ResponseEntity<>(new ApiResponse(true, "Item has been removed!"), HttpStatus.OK);
    }
}
