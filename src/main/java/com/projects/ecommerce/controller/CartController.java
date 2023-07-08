package com.projects.ecommerce.controller;

import com.projects.ecommerce.common.ApiResponse;
import com.projects.ecommerce.dto.cart.AddToCartDto;
import com.projects.ecommerce.dto.cart.CartDto;
import com.projects.ecommerce.model.User;
import com.projects.ecommerce.service.AuthenticationService;
import com.projects.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCart(@RequestBody AddToCartDto addToCartDto, @RequestParam("token") String token) {
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        cartService.addToCart(addToCartDto, user);
        return new ResponseEntity<>(new ApiResponse(true, "Added to cart!!"), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<CartDto> getCartItems(@RequestParam("token") String token) {
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        CartDto cartDto = cartService.listCartItems(user);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable("cartItemId") Integer itemId, @RequestParam("token") String token) {
        authenticationService.authenticate(token);
        User user = authenticationService.getUser(token);
        cartService.deleteCartItem(itemId, user);
        return new ResponseEntity<>(new ApiResponse(true, "Item has been removed!"), HttpStatus.OK);
    }
}
