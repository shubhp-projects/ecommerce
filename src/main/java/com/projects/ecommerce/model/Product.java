package com.projects.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "products")
public class Product {

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "category_id")
    Category category;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private @NotNull String name;

    private @NotNull String imageUrl;

    private @NotNull double price;

    private @NotNull String description;
}
