package com.projects.ecommerce.service;

import com.projects.ecommerce.dto.cart.AddToCartDto;
import com.projects.ecommerce.dto.cart.CartDto;
import com.projects.ecommerce.dto.cart.CartItemDto;
import com.projects.ecommerce.exceptions.CustomException;
import com.projects.ecommerce.model.Cart;
import com.projects.ecommerce.model.Product;
import com.projects.ecommerce.model.User;
import com.projects.ecommerce.repository.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    ProductService productService;

    @Autowired
    CartRepo cartRepo;

    public void addToCart(AddToCartDto addToCartDto, User user) {

        Product product = productService.findById(addToCartDto.getProductId());

        Cart cart = new Cart();
        cart.setProduct(product);
        cart.setUser(user);
        cart.setQuantity(addToCartDto.getQuantity());
        cart.setCreatedDate(new Date());

        cartRepo.save(cart);
    }

    public CartDto listCartItems(User user) {

        List<Cart> cartList = cartRepo.findAllByUserOrderByCreatedDateDesc(user);
        List<CartItemDto> cartItems = new ArrayList<>();
        double totalCost = 0;

        for (Cart cart : cartList) {
            CartItemDto cartItemDto = new CartItemDto(cart);
            totalCost += cartItemDto.getQuantity() * cart.getProduct().getPrice();
            cartItems.add(cartItemDto);
        }
        CartDto cartDto = new CartDto();
        cartDto.setTotalCost(totalCost);
        cartDto.setCartItems(cartItems);
        return cartDto;
    }

    public void deleteCartItem(Integer cartItemId, User user) {

        Optional<Cart> optionalCart = cartRepo.findById(cartItemId);

        if (!optionalCart.isPresent()) {
            throw new CustomException("Cart item ID is invalid: " + cartItemId);
        }
        Cart cart = optionalCart.get();

        if (cart.getUser() != user) {
            throw new CustomException("Cart item does not belong to user: " + user.getFirstName());
        }
        cartRepo.delete(cart);
    }
}
