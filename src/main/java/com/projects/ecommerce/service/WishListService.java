package com.projects.ecommerce.service;

import com.projects.ecommerce.dto.ProductDto;
import com.projects.ecommerce.model.User;
import com.projects.ecommerce.model.WishList;
import com.projects.ecommerce.repository.WishListRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishListService {

    @Autowired
    WishListRepo wishListRepo;

    @Autowired
    ProductService productService;

    public void createWishList(WishList wishList) {
        wishListRepo.save(wishList);
    }

    public List<ProductDto> getWishListForUser(User user) {
        List<WishList> wishLists = wishListRepo.findAllByUserOrderByCreatedDateDesc(user);
        List<ProductDto> productDtos = new ArrayList<>();
        for (WishList wishList : wishLists) {
            productDtos.add(productService.convertEntityToDto(wishList.getProduct()));
        }
        return productDtos;
    }
}
