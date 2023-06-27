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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    AuthenticationService authenticationService;

    @Transactional
    public ResponseDto signup(SignupDto signupDto) throws NoSuchAlgorithmException {
        if (Objects.nonNull(userRepo.findByEmail(signupDto.getEmail()))) {
            throw new CustomException("User already present!");
        }
        String encryptedPassword = hashPassword(signupDto.getPassword());
        User user = new User(signupDto.getFirstName(), signupDto.getLastName(), signupDto.getEmail(), encryptedPassword);
        userRepo.save(user);
        AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);
        return new ResponseDto("success", "User created successfully.");
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(password.getBytes());
        byte[] digest = messageDigest.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }

    public SignInResponseDto signIn(SignInDto signInDto) throws NoSuchAlgorithmException {
        User user = userRepo.findByEmail(signInDto.getEmail());
        if (Objects.isNull(user)) {
            throw new AuthenticationFailException("User is not valid!");
        }

        if (!user.getPassword().equals(hashPassword(signInDto.getPassword()))) {
            throw new AuthenticationFailException(("Incorrect Password!"));
        }
        AuthenticationToken token = authenticationService.getToken(user);

        if (Objects.isNull(token)) {
            throw new CustomException("Token is not present!");
        }

        return new SignInResponseDto("Success", token.getToken());
    }
}
