package com.subash.user.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subash.user.management.config.TestSecurityConfig;
import com.subash.user.management.model.AllUserResponse;
import com.subash.user.management.model.UserResponse;
import com.subash.user.management.model.UserView;
import com.subash.user.management.service.UserService;
import com.subash.user.management.util.GenericLogger;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private GenericLogger genericLogger;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_Positive() throws Exception {
        UserView userView = new UserView();
        userView.setRole(UserView.RoleEnum.USER);
        userView.setUsername("Subish");
        userView.setPassword("testUser");

        UserResponse response = new UserResponse();
        response.setCode(5001);
        response.setMessage("User created");

        Mockito.when(userService.createUser(anyString(), any(UserView.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        mockMvc.perform(post("/v1/api/users/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userView)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User created"))
                .andDo(print());
    }

    @WithMockUser(username = "subi", roles = "USER")
    @Test
    void getUser_Positive() throws Exception {
        String username = "subi";
        UserResponse response = new UserResponse();
        response.setCode(5002);
        response.setMessage("User fetched");

        Mockito.when(userService.getUser(anyString(), eq(username)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        mockMvc.perform(get("/v1/api/users/" + username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User fetched"))
                .andDo(print());
    }

    @Test
    void getUser_AccessDenied() throws Exception {
        setAuthenticatedUsername("wrongUser");

        mockMvc.perform(get("/v1/api/users/subi"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("403"))
                .andExpect(jsonPath("$.message").value("Access denied: you can only access your own data"))
                .andDo(print());
    }

    @Test
    void getAllUsers_Positive() throws Exception {
        AllUserResponse response = new AllUserResponse();
        response.setMessage("Fetched All");

        Mockito.when(userService.getAllUser(anyString()))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        mockMvc.perform(get("/v1/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Fetched All"))
                .andDo(print());
    }

    @Test
    void removeUser_Positive() throws Exception {
        String username = "subi";

        UserResponse response = new UserResponse();
        response.setMessage("User Removed");

        Mockito.when(userService.removeUser(anyString(), eq(username)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        mockMvc.perform(delete("/v1/api/users/" + username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User Removed"))
                .andDo(print());
    }

    @Test
    void createUser_InvalidPayload() throws Exception {
        UserView userView = new UserView(); // Assume required fields missing

        mockMvc.perform(post("/v1/api/users/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userView)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    private void setAuthenticatedUsername(String username) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // or your actual role
                );

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}