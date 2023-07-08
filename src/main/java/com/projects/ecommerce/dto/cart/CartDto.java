package com.projects.ecommerce.dto.cart;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartDto {
    List<CartItemDto> cartItems;
    private double totalCost;

}
