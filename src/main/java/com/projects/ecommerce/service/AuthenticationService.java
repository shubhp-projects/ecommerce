package com.projects.ecommerce.service;

import com.projects.ecommerce.exceptions.AuthenticationFailException;
import com.projects.ecommerce.model.AuthenticationToken;
import com.projects.ecommerce.model.User;
import com.projects.ecommerce.repository.TokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    public User getUser(String token) {
        AuthenticationToken authenticationToken = tokenRepo.findByToken(token);
        if (Objects.isNull(authenticationToken)) {
            return null;
        }
        return authenticationToken.getUser();
    }

    public void authenticate(String token) throws AuthenticationFailException {
        if (Objects.isNull(token)) {
            throw new AuthenticationFailException("Token not present!");
        }
        if (Objects.isNull(getUser(token))) {
            throw new AuthenticationFailException("Token not valid!");
        }
    }
}
