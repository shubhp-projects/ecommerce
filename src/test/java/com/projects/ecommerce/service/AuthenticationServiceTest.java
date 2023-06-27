package com.projects.ecommerce.service;

import com.projects.ecommerce.model.AuthenticationToken;
import com.projects.ecommerce.model.User;
import com.projects.ecommerce.repository.TokenRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.verify;

class AuthenticationServiceTest {

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


    @Test
    void saveConfirmationTokenTest() {
        TokenRepo tokenRepo = Mockito.mock(TokenRepo.class);
        AuthenticationService authenticationService = new AuthenticationService();
        ReflectionTestUtils.setField(authenticationService, "tokenRepo", tokenRepo);
        List<AuthenticationToken> authenticationTokenList = createDummyTokenData();
        authenticationService.saveConfirmationToken(authenticationTokenList.get(0));
        verify(tokenRepo).save(authenticationTokenList.get(0));
    }

    @Test
    void getTokenTest() {
        TokenRepo tokenRepo = Mockito.mock(TokenRepo.class);
        AuthenticationService authenticationService = new AuthenticationService();
        ReflectionTestUtils.setField(authenticationService, "tokenRepo", tokenRepo);
        List<AuthenticationToken> authenticationTokenList = createDummyTokenData();
        authenticationService.getToken(authenticationTokenList.get(0).getUser());
        verify(tokenRepo).findByUser(authenticationTokenList.get(0).getUser());
    }
}