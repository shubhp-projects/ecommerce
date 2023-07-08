package com.projects.ecommerce.controller;

import com.projects.ecommerce.common.ApiResponse;
import com.projects.ecommerce.dto.cart.AddToCartDto;
import com.projects.ecommerce.dto.cart.CartDto;
import com.projects.ecommerce.dto.cart.CartItemDto;
import com.projects.ecommerce.model.*;
import com.projects.ecommerce.service.AuthenticationService;
import com.projects.ecommerce.service.CartService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class CartControllerTest {

    private List<Category> createDummyCategoryData() {
        List<Category> categoryList = new ArrayList<>();
        Category category = new Category();
        category.setCategoryName("TestCategory");
        category.setId(1);
        category.setDescription("Test category");
        category.setImageUrl("img/url");
        categoryList.add(category);
        return categoryList;
    }

    private List<Product> createDummyProductData() {
        List<Product> productList = new ArrayList<>();
        Product product = new Product();
        product.setCategory(createDummyCategoryData().get(0));
        product.setName("TestProduct");
        product.setDescription("Test product");
        product.setId(1);
        product.setPrice(100);
        product.setImageUrl("img/url");
        productList.add(product);
        return productList;
    }

    private List<User> createDummyUserData() {
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setId(1);
        user.setFirstName("TestFirstName");
        user.setLastName("TestLastName");
        user.setEmail("email@test.com");
        user.setPassword("password");
        userList.add(user);
        return userList;
    }

    private List<AuthenticationToken> createDummyTokenData() {
        List<AuthenticationToken> authenticationTokenList = new ArrayList<>();
        AuthenticationToken authenticationToken = new AuthenticationToken();
        authenticationToken.setId(1);
        authenticationToken.setToken("TestToken");
        authenticationToken.setCreatedDate(new Date(1 / 2 / 2023));
        authenticationToken.setUser(createDummyUserData().get(0));
        authenticationTokenList.add(authenticationToken);
        return authenticationTokenList;
    }

    private List<AddToCartDto> createDummyAddToDtoData() {
        List<AddToCartDto> addToCartDtoList = new ArrayList<>();
        AddToCartDto addToCartDto = new AddToCartDto();
        addToCartDto.setId(1);
        addToCartDto.setProductId(createDummyProductData().get(0).getId());
        addToCartDto.setQuantity(1);
        addToCartDtoList.add(addToCartDto);
        return addToCartDtoList;
    }

    private List<CartItemDto> createDummyCartItemDtoData() {
        List<CartItemDto> cartItemDtoList = new ArrayList<>();
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(1);
        cartItemDto.setProduct(createDummyProductData().get(0));
        cartItemDto.setQuantity(1);
        cartItemDtoList.add(cartItemDto);
        return cartItemDtoList;
    }

    private List<CartDto> createDummyCartDto() {
        List<CartDto> cartDtoList = new ArrayList<>();
        CartDto cartDto = new CartDto();
        cartDto.setCartItems(createDummyCartItemDtoData());
        cartDto.setTotalCost(createDummyCartItemDtoData().get(0).getQuantity() * createDummyCartItemDtoData().get(0).getProduct().getPrice());
        cartDtoList.add(cartDto);
        return cartDtoList;
    }

    private List<Cart> createDummyCartData() {
        List<Cart> cartList = new ArrayList<>();
        Cart cart = new Cart();
        cart.setId(createDummyAddToDtoData().get(0).getId());
        cart.setUser(createDummyUserData().get(0));
        cart.setProduct(createDummyProductData().get(0));
        cart.setQuantity(createDummyAddToDtoData().get(0).getQuantity());
        cart.setCreatedDate(new Date());
        cartList.add(cart);
        return cartList;
    }

    @Test
    void addToCartTest() {
        CartController cartController = new CartController();
        AuthenticationService authenticationService = Mockito.mock(AuthenticationService.class);
        CartService cartService = Mockito.mock(CartService.class);
        ReflectionTestUtils.setField(cartController, "authenticationService", authenticationService);
        ReflectionTestUtils.setField(cartController, "cartService", cartService);
        List<AuthenticationToken> tokenList = createDummyTokenData();
        List<AddToCartDto> addToCartDtoList = createDummyAddToDtoData();
        doNothing().when(authenticationService).authenticate(tokenList.get(0).getToken());
        when(authenticationService.getUser(tokenList.get(0).getToken())).thenReturn(tokenList.get(0).getUser());
        ResponseEntity<ApiResponse> response = cartController.addToCart(addToCartDtoList.get(0), tokenList.get(0).getToken());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void getCartItemsTest() {
        CartController cartController = new CartController();
        AuthenticationService authenticationService = Mockito.mock(AuthenticationService.class);
        CartService cartService = Mockito.mock(CartService.class);
        ReflectionTestUtils.setField(cartController, "authenticationService", authenticationService);
        ReflectionTestUtils.setField(cartController, "cartService", cartService);
        List<AuthenticationToken> tokenList = createDummyTokenData();
        List<CartDto> cartDtoList = createDummyCartDto();
        List<User> userList = createDummyUserData();
        doNothing().when(authenticationService).authenticate(tokenList.get(0).getToken());
        when(authenticationService.getUser(tokenList.get(0).getToken())).thenReturn(tokenList.get(0).getUser());
        when(cartService.listCartItems(userList.get(0))).thenReturn(cartDtoList.get(0));
        ResponseEntity<CartDto> response = cartController.getCartItems(tokenList.get(0).getToken());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteCartItemTest() {
        CartController cartController = new CartController();
        AuthenticationService authenticationService = Mockito.mock(AuthenticationService.class);
        CartService cartService = Mockito.mock(CartService.class);
        ReflectionTestUtils.setField(cartController, "authenticationService", authenticationService);
        ReflectionTestUtils.setField(cartController, "cartService", cartService);
        List<AuthenticationToken> tokenList = createDummyTokenData();
        List<Cart> cartList = createDummyCartData();
        doNothing().when(authenticationService).authenticate(tokenList.get(0).getToken());
        when(authenticationService.getUser(tokenList.get(0).getToken())).thenReturn(tokenList.get(0).getUser());
        doNothing().when(cartService).deleteCartItem(cartList.get(0).getId(), tokenList.get(0).getUser());
        ResponseEntity<ApiResponse> response = cartController.deleteCartItem(cartList.get(0).getId(), tokenList.get(0).getToken());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Item has been removed!", Objects.requireNonNull(response.getBody()).getMessage());
    }
}