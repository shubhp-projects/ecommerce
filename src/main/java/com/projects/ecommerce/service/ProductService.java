package com.projects.ecommerce.service;

import com.projects.ecommerce.dto.ProductDto;
import com.projects.ecommerce.exceptions.ProductNotExistsException;
import com.projects.ecommerce.model.Product;
import com.projects.ecommerce.repository.ProductRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepo productRepo;

    @Autowired
    ModelMapper modelMapper;

    public ProductDto createProduct(ProductDto productDto) {
        Product product = convertDtoToEntity(productDto);
        productRepo.save(product);
        return convertEntityToDto(product);
    }

    public List<ProductDto> getAllProducts() {
        List<Product> allProducts = productRepo.findAll();

        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : allProducts) {
            productDtos.add(convertEntityToDto(product));
        }
        return productDtos;
    }

    public ProductDto updateProduct(ProductDto productDto) {
        Product product = productRepo.findById(productDto.getId()).orElseThrow(() -> new ProductNotExistsException("Product does not exists!"));
        product.setDescription(productDto.getDescription());
        product.setImageUrl(productDto.getImageUrl());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        productRepo.save(product);

        return convertEntityToDto(product);
    }

    public void deleteProduct(Integer productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new ProductNotExistsException("Product does not exists!"));
        productRepo.delete(product);
    }

    public ProductDto convertEntityToDto(Product result) {
        return modelMapper.map(result, ProductDto.class);
    }

    public Product convertDtoToEntity(ProductDto result) {
        return modelMapper.map(result, Product.class);
    }
}
