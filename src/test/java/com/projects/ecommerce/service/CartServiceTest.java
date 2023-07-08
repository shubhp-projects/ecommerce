package com.projects.ecommerce.service;

import com.projects.ecommerce.dto.cart.AddToCartDto;
import com.projects.ecommerce.dto.cart.CartItemDto;
import com.projects.ecommerce.exceptions.CustomException;
import com.projects.ecommerce.model.Cart;
import com.projects.ecommerce.model.Category;
import com.projects.ecommerce.model.Product;
import com.projects.ecommerce.model.User;
import com.projects.ecommerce.repository.CartRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CartServiceTest {

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

    private List<AddToCartDto> createDummyAddToDtoData() {
        List<AddToCartDto> addToCartDtoList = new ArrayList<>();
        AddToCartDto addToCartDto = new AddToCartDto();
        addToCartDto.setId(1);
        addToCartDto.setProductId(createDummyProductData().get(0).getId());
        addToCartDto.setQuantity(1);
        addToCartDtoList.add(addToCartDto);
        return addToCartDtoList;
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

    private List<CartItemDto> createDummyCartItemDto() {
        List<CartItemDto> cartItemDtoList = new ArrayList<>();
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(createDummyCartData().get(0).getId());
        cartItemDto.setProduct(createDummyProductData().get(0));
        cartItemDto.setQuantity(createDummyCartData().get(0).getQuantity());
        cartItemDtoList.add(cartItemDto);
        return cartItemDtoList;
    }

    @Test
    void addToCartTest() {
        ProductService productService = Mockito.mock(ProductService.class);
        CartRepo cartRepo = Mockito.mock(CartRepo.class);
        CartService cartService = new CartService();
        ReflectionTestUtils.setField(cartService, "productService", productService);
        ReflectionTestUtils.setField(cartService, "cartRepo", cartRepo);
        List<Product> productList = createDummyProductData();
        List<AddToCartDto> addToCartDtoList = createDummyAddToDtoData();
        List<User> userList = createDummyUserData();
        when(productService.findById(productList.get(0).getId())).thenReturn(productList.get(0));
        cartService.addToCart(addToCartDtoList.get(0), userList.get(0));
        verify(cartRepo).save(any(Cart.class));
    }


    @Test
    void listCartItemsTest() {
        ProductService productService = Mockito.mock(ProductService.class);
        CartRepo cartRepo = Mockito.mock(CartRepo.class);
        CartService cartService = new CartService();
        ReflectionTestUtils.setField(cartService, "productService", productService);
        ReflectionTestUtils.setField(cartService, "cartRepo", cartRepo);
        List<Cart> cartList = createDummyCartData();
        List<User> userList = createDummyUserData();
        List<CartItemDto> cartItemDtoList = createDummyCartItemDto();
        double totalCost = 0;
        when(cartRepo.findAllByUserOrderByCreatedDateDesc(userList.get(0))).thenReturn(cartList);
        for (Cart cart : cartList) {
            totalCost += cartItemDtoList.get(0).getQuantity() * cart.getProduct().getPrice();
            cartItemDtoList.add(cartItemDtoList.get(0));
        }

        cartService.listCartItems(userList.get(0));
        double expectedTotalCost = 100;
        Assertions.assertEquals(expectedTotalCost, totalCost);

    }

    @Test
    void deleteCartItemTest() {
        ProductService productService = Mockito.mock(ProductService.class);
        CartRepo cartRepo = Mockito.mock(CartRepo.class);
        CartService cartService = new CartService();
        ReflectionTestUtils.setField(cartService, "productService", productService);
        ReflectionTestUtils.setField(cartService, "cartRepo", cartRepo);
        List<Cart> cartList = createDummyCartData();
        List<User> userList = createDummyUserData();
        cartList.get(0).setUser(userList.get(0));
        Optional<Cart> optionalCart = Optional.ofNullable(cartList.get(0));
        when(cartRepo.findById(cartList.get(0).getId())).thenReturn(optionalCart);
        cartService.deleteCartItem(cartList.get(0).getId(), userList.get(0));
        verify(cartRepo).delete(cartList.get(0));
    }

    @Test
    void deleteCartItemWithInvalidItemTest() {
        ProductService productService = Mockito.mock(ProductService.class);
        CartRepo cartRepo = Mockito.mock(CartRepo.class);
        CartService cartService = new CartService();
        ReflectionTestUtils.setField(cartService, "productService", productService);
        ReflectionTestUtils.setField(cartService, "cartRepo", cartRepo);
        List<Cart> cartList = createDummyCartData();
        List<User> userList = createDummyUserData();
        User user = userList.get(0);
        cartList.get(0).setUser(user);
        Optional<Cart> optionalCart = Optional.empty();
        Integer cartItemId = cartList.get(0).getId();
        when(cartRepo.findById(cartItemId)).thenReturn(optionalCart);
        CustomException thrown = assertThrows(CustomException.class, () -> cartService.deleteCartItem(cartItemId, user));
        assertEquals(String.format("Cart item ID is invalid: %d", cartItemId), thrown.getMessage());
    }

    @Test
    void deleteCartItemWithInvalidUserTest() {
        ProductService productService = Mockito.mock(ProductService.class);
        CartRepo cartRepo = Mockito.mock(CartRepo.class);
        CartService cartService = new CartService();
        ReflectionTestUtils.setField(cartService, "productService", productService);
        ReflectionTestUtils.setField(cartService, "cartRepo", cartRepo);
        List<Cart> cartList = createDummyCartData();
        List<User> userList = createDummyUserData();
        User user = userList.get(0);
        cartList.get(0).setUser(user);
        Optional<Cart> optionalCart = Optional.of(cartList.get(0));
        User invalidUser = new User();
        Integer cartItemId = cartList.get(0).getId();
        when(cartRepo.findById(cartItemId)).thenReturn(optionalCart);
        CustomException thrown = assertThrows(CustomException.class, () -> cartService.deleteCartItem(cartItemId, invalidUser));
        assertEquals("Cart item does not belong to user: null", thrown.getMessage());
    }
}