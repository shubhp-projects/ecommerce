package com.projects.ecommerce.controller;

import com.projects.ecommerce.dto.ResponseDto;
import com.projects.ecommerce.dto.user.SignInDto;
import com.projects.ecommerce.dto.user.SignInResponseDto;
import com.projects.ecommerce.dto.user.SignupDto;
import com.projects.ecommerce.model.AuthenticationToken;
import com.projects.ecommerce.model.User;
import com.projects.ecommerce.repository.UserRepo;
import com.projects.ecommerce.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class UserControllerTest {

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

    private List<SignupDto> createDummySignUpData() {
        List<SignupDto> signupDtoList = new ArrayList<>();
        SignupDto signupDto = new SignupDto();
        signupDto.setFirstName("TestFirstName");
        signupDto.setLastName("TestLastName");
        signupDto.setEmail("email@test.com");
        signupDto.setPassword("password");
        signupDtoList.add(signupDto);
        return signupDtoList;
    }

    private List<SignInDto> createDummySignInData() {
        List<SignInDto> signInDtoList = new ArrayList<>();
        SignInDto signInDto = new SignInDto("email@test.com", "password");
        signInDtoList.add(signInDto);
        return signInDtoList;
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
    void signupTest() throws NoSuchAlgorithmException {
        UserService userService = Mockito.mock(UserService.class);
        UserController userController = new UserController();
        ReflectionTestUtils.setField(userController, "userService", userService);
        UserRepo userRepo = Mockito.mock(UserRepo.class);
        ReflectionTestUtils.setField(userService, "userRepo", userRepo);
        List<SignupDto> signupDtoList = createDummySignUpData();
        ResponseDto responseDto = new ResponseDto("success", "User created successfully.");
        when(userService.signup(signupDtoList.get(0))).thenReturn(responseDto);
        ResponseDto response = userController.signup(signupDtoList.get(0));
        assertNotNull(response);
    }

    @Test
    void signInTest() throws NoSuchAlgorithmException {
        UserService userService = Mockito.mock(UserService.class);
        UserController userController = new UserController();
        ReflectionTestUtils.setField(userController, "userService", userService);
        UserRepo userRepo = Mockito.mock(UserRepo.class);
        ReflectionTestUtils.setField(userService, "userRepo", userRepo);
        List<SignInDto> signInDtoList = createDummySignInData();
        List<AuthenticationToken> authenticationTokenList = createDummyTokenData();
        SignInResponseDto signInResponseDto = new SignInResponseDto("success", authenticationTokenList.get(0).getToken());
        when(userService.signIn(signInDtoList.get(0))).thenReturn(signInResponseDto);
        SignInResponseDto response = userController.signIn(signInDtoList.get(0));
        assertNotNull(response);
    }
}