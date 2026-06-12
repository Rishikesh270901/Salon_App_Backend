package com.Rishikesh.UserService.service;


import com.Rishikesh.UserService.payload.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.Scope;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class KeycloakService {

    private static final String KEYCLOAK_BASE_URL = "http://localhost:8080";

    private static final String KEYCLOAK_ADMIN_API = KEYCLOAK_BASE_URL+"/admin/realms/master/users";

    private static final String TOKEN_API = KEYCLOAK_BASE_URL+"/realms/master/protocol/openid-connect/token";

    private static final String CLIENT_ID = "salon-booking-client";

    private static final String CLIENT_SECRET = "0wMK6tJDRBaV1nSYlVUiPiEPibJwQN1f";

    private static final String GRANT_TYPE = "password";

    private static final String SCOPE = "openid profile email";

    private static final String username = "rishikesh";

    private static final String password = "admin";

    private static final String clientId = "41b1237c-fd5c-446d-874d-b360d93db417";


    private final RestTemplate restTemplate;


    public void createUser(SignupDTO signupDTO) throws Exception{

        String ACCESS_TOKEN = getAdminAccessToken(username, password, GRANT_TYPE, null)
                .getAccessToken();

        Credential credential = new Credential();

        credential.setTemporary(false);
        credential.setType("password");
        credential.setValue(signupDTO.getPassword());

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(signupDTO.getUsername());
        userRequest.setEmail(signupDTO.getEmail());
        userRequest.setFirstName(signupDTO.getFullName());
        userRequest.getCredentials().add(credential);
        userRequest.setEnabled(true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);

        HttpEntity<UserRequest> requestHttpEntity = new HttpEntity<>(userRequest, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                KEYCLOAK_ADMIN_API,
                HttpMethod.POST,
                requestHttpEntity,
                String.class
        );

        if(response.getStatusCode()==HttpStatus.CREATED){
            System.out.println("User created successfully");

            KeycloakUserDTO user = fetchFirstUserByUsername(signupDTO.getUsername(), ACCESS_TOKEN);

            KeycloakRole role = getRoleByName(clientId, ACCESS_TOKEN, signupDTO.getRole().toString());

            List<KeycloakRole> roles = new ArrayList<>();
            roles.add(role);

            assignRoleToUser(user.getId(),
                    clientId,
                    roles,
                    ACCESS_TOKEN);
        }
        else{
            System.out.println("User creation failed");
            throw new Exception(response.getBody());
        }

    }

    public TokenResponse getAdminAccessToken(String username, String password,
                                             String grantType, String refreshToken) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", GRANT_TYPE);
        requestBody.add("username", username);
        requestBody.add("password", password);
        if (refreshToken != null) {
            requestBody.add("refresh_token", refreshToken);
        }
        requestBody.add("client_id", CLIENT_ID);
        requestBody.add("client_secret", CLIENT_SECRET);
        requestBody.add("scope", SCOPE);

        HttpEntity<MultiValueMap<String, String>> requestHttpEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<TokenResponse> response = restTemplate.exchange(
                TOKEN_API,
                HttpMethod.POST,
                requestHttpEntity,
                TokenResponse.class
        );

        if(response.getStatusCode()==HttpStatus.OK && response.getBody()!=null){
            return response.getBody();
        }
        else{
            throw new Exception("Failed to obtain access token: " + response.getBody());
        }
    }

    public KeycloakRole getRoleByName(String clientId, String token, String role){

        String url = KEYCLOAK_BASE_URL+"/admin/realms/master/clients/" + clientId + "/roles/" + role;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestHttpEntity = new HttpEntity<>(headers);

        ResponseEntity<KeycloakRole> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestHttpEntity,
                KeycloakRole.class
        );

            return response.getBody();

    }

    public KeycloakUserDTO fetchFirstUserByUsername(String username, String token) throws Exception {

        String url = KEYCLOAK_BASE_URL+"/admin/realms/master/users?username="+username;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestHttpEntity = new HttpEntity<>(headers);

        ResponseEntity<KeycloakUserDTO[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestHttpEntity,
                KeycloakUserDTO[].class
        );

        KeycloakUserDTO[] users = response.getBody();

        if(users != null && users.length>0){
            return users[0];
        }

        throw new Exception("User not found with username: "+username);

    }

    public void assignRoleToUser(String userId, String clientId, List<KeycloakRole> roles, String token) throws Exception {

        String url = KEYCLOAK_BASE_URL+"/admin/realms/master/users/"+userId+"/role-mappings/clients/"+clientId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<KeycloakRole>> requestHttpEntity = new HttpEntity<>(roles, headers);

        try{
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestHttpEntity,
                    String.class
            );
        }catch (Exception e){
           throw new Exception("Failed to assign role" + e.getMessage());
        }

    }

}
