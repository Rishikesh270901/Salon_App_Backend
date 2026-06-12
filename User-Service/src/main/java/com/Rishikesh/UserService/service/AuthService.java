package com.Rishikesh.UserService.service;

import com.Rishikesh.UserService.payload.dto.SignupDTO;
import com.Rishikesh.UserService.payload.response.AuthResponse;

public interface AuthService {

    AuthResponse login(String username, String password) throws Exception;

    AuthResponse signup(SignupDTO req) throws Exception;

    AuthResponse getAccessTokenFromRefreshToken(String refreshToken) throws Exception;

}
