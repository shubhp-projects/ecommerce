package com.projects.ecommerce.repository;

import com.projects.ecommerce.model.AuthenticationToken;
import com.projects.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepo extends JpaRepository<AuthenticationToken, Integer> {

    AuthenticationToken findByUser(User user);

    AuthenticationToken findByToken(String token);
}
