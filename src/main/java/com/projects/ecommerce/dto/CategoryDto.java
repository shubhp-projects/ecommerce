package com.projects.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CategoryDto {

    private Integer id;

    @NotBlank
    private String categoryName;

    @NotBlank
    private String description;

    @NotBlank()
    private String imageUrl;
}
