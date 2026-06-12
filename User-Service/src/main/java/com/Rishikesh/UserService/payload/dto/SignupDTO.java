package com.Rishikesh.UserService.payload.dto;

import com.Rishikesh.UserService.domain.UserRole;
import lombok.Data;

@Data
public class SignupDTO {

    private String fullName;

    private String password;

    private String username;

    private String email;

    private UserRole role;

}
