package com.projects.ecommerce.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignInResponseDto {
    private String status;
    private String token;
}
