package com.projects.ecommerce.service;

import com.projects.ecommerce.model.AuthenticationToken;
import com.projects.ecommerce.model.User;
import com.projects.ecommerce.repository.TokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    TokenRepo tokenRepo;

    public void saveConfirmationToken(AuthenticationToken authenticationToken) {
        tokenRepo.save(authenticationToken);
    }

    public AuthenticationToken getToken(User user) {
        return tokenRepo.findByUser(user);
    }
}
