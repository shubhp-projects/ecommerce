package com.projects.ecommerce.repository;

import com.projects.ecommerce.model.User;
import com.projects.ecommerce.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepo extends JpaRepository<WishList, Integer> {

    List<WishList> findAllByUserOrderByCreatedDateDesc(User user);
}
