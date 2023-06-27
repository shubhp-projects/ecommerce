package com.projects.ecommerce.service;

import com.projects.ecommerce.dto.ResponseDto;
import com.projects.ecommerce.dto.user.SignInDto;
import com.projects.ecommerce.dto.user.SignInResponseDto;
import com.projects.ecommerce.dto.user.SignupDto;
import com.projects.ecommerce.exceptions.AuthenticationFailException;
import com.projects.ecommerce.exceptions.CustomException;
import com.projects.ecommerce.model.AuthenticationToken;
import com.projects.ecommerce.model.User;
import com.projects.ecommerce.repository.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class UserServiceTest {

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
        UserRepo userRepo = Mockito.mock(UserRepo.class);
        AuthenticationService authenticationService = Mockito.mock(AuthenticationService.class);
        UserService userService = new UserService();
        ReflectionTestUtils.setField(userService, "userRepo", userRepo);
        ReflectionTestUtils.setField(userService, "authenticationService", authenticationService);
        List<User> userList = createDummyUserData();
        List<SignupDto> signupDtoList = createDummySignUpData();
        when(userRepo.save(Mockito.any(User.class))).thenReturn(userList.get(0));
        doNothing().when(authenticationService).saveConfirmationToken(Mockito.any(AuthenticationToken.class));
        ResponseDto response = userService.signup(signupDtoList.get(0));
        assertNotNull(response);
    }

    @Test
    void signupExceptionTest() {
        UserRepo userRepo = Mockito.mock(UserRepo.class);
        UserService userService = new UserService();
        ReflectionTestUtils.setField(userService, "userRepo", userRepo);
        List<User> userList = createDummyUserData();
        List<SignupDto> signupDtoList = createDummySignUpData();
        SignupDto signupDto = signupDtoList.get(0);
        when(userRepo.findByEmail(signupDto.getEmail())).thenReturn(userList.get(0));
        CustomException thrown = Assertions.assertThrows(CustomException.class, () -> userService.signup(signupDto));
        assertEquals("User already present!", thrown.getMessage());
    }

    @Test
    void signInForIncorrectPasswordTest() {
        UserRepo userRepo = Mockito.mock(UserRepo.class);
        UserService userService = new UserService();
        ReflectionTestUtils.setField(userService, "userRepo", userRepo);
        List<User> userList = createDummyUserData();
        List<SignInDto> signInDtoList = createDummySignInData();
        SignInDto signInDto = signInDtoList.get(0);
        when(userRepo.findByEmail(signInDto.getEmail())).thenReturn(userList.get(0));
        when(userRepo.findByEmail(signInDto.getPassword())).thenReturn(Mockito.any());
        AuthenticationFailException thrown = Assertions.assertThrows(AuthenticationFailException.class, () -> userService.signIn(signInDto));
        assertEquals("Incorrect Password!", thrown.getMessage());
    }

    @Test
    void signInForUserInvalidTest() {
        UserRepo userRepo = Mockito.mock(UserRepo.class);
        UserService userService = new UserService();
        ReflectionTestUtils.setField(userService, "userRepo", userRepo);
        List<SignInDto> signInDtoList = createDummySignInData();
        SignInDto signInDto = signInDtoList.get(0);
        when(userRepo.findByEmail(signInDto.getEmail())).thenReturn(null);
        AuthenticationFailException thrown = Assertions.assertThrows(AuthenticationFailException.class, () -> userService.signIn(signInDto));
        assertEquals("User is not valid!", thrown.getMessage());
    }

    @Test
    void signInSuccessTest() throws NoSuchAlgorithmException {
        UserRepo userRepo = Mockito.mock(UserRepo.class);
        UserService userService = new UserService();
        ReflectionTestUtils.setField(userService, "userRepo", userRepo);
        AuthenticationService authenticationService = Mockito.mock(AuthenticationService.class);
        ReflectionTestUtils.setField(userService, "authenticationService", authenticationService);
        List<SignInDto> signInDtoList = createDummySignInData();
        List<User> userList = createDummyUserData();
        SignInDto signInDto = signInDtoList.get(0);
        userList.get(0).setPassword("5F4DCC3B5AA765D61D8327DEB882CF99");
        List<AuthenticationToken> authenticationTokenList = createDummyTokenData();
        when(userRepo.findByEmail(signInDto.getEmail())).thenReturn(userList.get(0));
        when(authenticationService.getToken(Mockito.any(User.class))).thenReturn(authenticationTokenList.get(0));
        SignInResponseDto response = userService.signIn(signInDto);
        assertNotNull(response);
    }

    @Test
    void signInInvalidTokenTest() {
        UserRepo userRepo = Mockito.mock(UserRepo.class);
        UserService userService = new UserService();
        ReflectionTestUtils.setField(userService, "userRepo", userRepo);
        AuthenticationService authenticationService = Mockito.mock(AuthenticationService.class);
        ReflectionTestUtils.setField(userService, "authenticationService", authenticationService);
        List<SignInDto> signInDtoList = createDummySignInData();
        List<User> userList = createDummyUserData();
        SignInDto signInDto = signInDtoList.get(0);
        userList.get(0).setPassword("5F4DCC3B5AA765D61D8327DEB882CF99");
        when(userRepo.findByEmail(signInDto.getEmail())).thenReturn(userList.get(0));
        when(authenticationService.getToken(Mockito.any(User.class))).thenReturn(null);
        CustomException thrown = Assertions.assertThrows(CustomException.class, () -> userService.signIn(signInDto));
        assertEquals("Token is not present!", thrown.getMessage());
    }
}