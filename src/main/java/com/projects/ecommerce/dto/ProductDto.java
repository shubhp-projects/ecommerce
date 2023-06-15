package com.projects.ecommerce.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
public class ProductDto {
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private String imageUrl;

    @NotNull
    private double price;

    @NotNull
    private String description;

    @NotNull
    private Integer categoryId;

}
