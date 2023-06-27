package com.projects.ecommerce.controller;

import com.projects.ecommerce.dto.ResponseDto;
import com.projects.ecommerce.dto.user.SignInDto;
import com.projects.ecommerce.dto.user.SignInResponseDto;
import com.projects.ecommerce.dto.user.SignupDto;
import com.projects.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RequestMapping("user")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody SignupDto signupDto) throws NoSuchAlgorithmException {
        return userService.signup(signupDto);
    }

    @PostMapping("/signin")
    public SignInResponseDto signIn(@RequestBody SignInDto signInDto) throws NoSuchAlgorithmException {
        return userService.signIn(signInDto);
    }
}
