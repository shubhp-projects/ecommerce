package com.projects.ecommerce.dto.cart;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class AddToCartDto {

    private Integer id;

    @NotNull
    private Integer productId;

    @NotNull
    private Integer quantity;
}
