package com.projects.ecommerce.controller;

import com.projects.ecommerce.common.ApiResponse;
import com.projects.ecommerce.dto.ProductDto;
import com.projects.ecommerce.model.*;
import com.projects.ecommerce.service.AuthenticationService;
import com.projects.ecommerce.service.WishListService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WishListControllerTest {

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
        product.setImageUrl("img/url");
        productList.add(product);
        return productList;
    }

    private List<WishList> createDummyWishListData() {
        List<WishList> wishLists = new ArrayList<>();
        WishList wishList = new WishList();
        wishList.setId(1);
        wishList.setUser(createDummyUserData().get(0));
        wishList.setProduct(createDummyProductData().get(0));
        wishList.setCreatedDate(new Date());
        wishLists.add(wishList);
        return wishLists;
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

    public List<ProductDto> createDummyProductDtoData() {
        List<ProductDto> productDtoList = new ArrayList<>();
        ProductDto productDto = new ProductDto();
        productDto.setCategoryId(createDummyCategoryData().get(0).getId());
        productDto.setName("TestProduct");
        productDto.setDescription("Test product");
        productDto.setId(1);
        productDto.setImageUrl("img/url");
        productDtoList.add(productDto);
        return productDtoList;
    }

    @Test
    void addToWishListTest() {
        WishListService wishListService = Mockito.mock(WishListService.class);
        AuthenticationService authenticationService = Mockito.mock(AuthenticationService.class);
        WishListController wishListController = new WishListController();
        ReflectionTestUtils.setField(wishListController, "wishListService", wishListService);
        ReflectionTestUtils.setField(wishListController, "authenticationService", authenticationService);
        List<User> userList = createDummyUserData();
        List<AuthenticationToken> authenticationTokenList = createDummyTokenData();
        List<Product> productList = createDummyProductData();
        List<WishList> wishLists = createDummyWishListData();
        doNothing().when(authenticationService).authenticate(authenticationTokenList.get(0).getToken());
        when(authenticationService.getUser(authenticationTokenList.get(0).getToken())).thenReturn(userList.get(0));
        doNothing().when(wishListService).createWishList(wishLists.get(0));
        ResponseEntity<ApiResponse> response = wishListController.addToWishList(productList.get(0), authenticationTokenList.get(0).getToken());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Added to wishlist.", Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    void getWishListTest() {
        WishListService wishListService = Mockito.mock(WishListService.class);
        AuthenticationService authenticationService = Mockito.mock(AuthenticationService.class);
        WishListController wishListController = new WishListController();
        ReflectionTestUtils.setField(wishListController, "wishListService", wishListService);
        ReflectionTestUtils.setField(wishListController, "authenticationService", authenticationService);
        List<User> userList = createDummyUserData();
        List<AuthenticationToken> authenticationTokenList = createDummyTokenData();
        List<ProductDto> productDtoList = createDummyProductDtoData();
        doNothing().when(authenticationService).authenticate(authenticationTokenList.get(0).getToken());
        when(authenticationService.getUser(authenticationTokenList.get(0).getToken())).thenReturn(userList.get(0));
        when(wishListService.getWishListForUser(userList.get(0))).thenReturn(productDtoList);
        ResponseEntity<List<ProductDto>> response = wishListController.getWishList(authenticationTokenList.get(0).getToken());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}