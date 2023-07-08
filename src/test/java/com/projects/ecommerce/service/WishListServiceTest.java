package com.projects.ecommerce.service;

import com.projects.ecommerce.model.Category;
import com.projects.ecommerce.model.Product;
import com.projects.ecommerce.model.User;
import com.projects.ecommerce.model.WishList;
import com.projects.ecommerce.repository.WishListRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WishListServiceTest {

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

    @Test
    void createWishListTest() {
        WishListRepo wishListRepo = Mockito.mock(WishListRepo.class);
        ProductService productService = Mockito.mock(ProductService.class);
        WishListService wishListService = new WishListService();
        ReflectionTestUtils.setField(wishListService, "wishListRepo", wishListRepo);
        ReflectionTestUtils.setField(wishListService, "productService", productService);
        List<WishList> wishLists = createDummyWishListData();
        wishListService.createWishList(wishLists.get(0));
        verify(wishListRepo).save(wishLists.get(0));
    }

    @Test
    void getWishListForUserTest() {
        WishListRepo wishListRepo = Mockito.mock(WishListRepo.class);
        ProductService productService = Mockito.mock(ProductService.class);
        WishListService wishListService = new WishListService();
        ReflectionTestUtils.setField(wishListService, "wishListRepo", wishListRepo);
        ReflectionTestUtils.setField(wishListService, "productService", productService);
        List<WishList> wishLists = createDummyWishListData();
        List<User> userList = createDummyUserData();
        when(wishListRepo.findAllByUserOrderByCreatedDateDesc(userList.get(0))).thenReturn(wishLists);
        wishListService.getWishListForUser(userList.get(0));
    }
}