package com.subash.user.management.service;

import com.subash.user.management.model.*;
import com.subash.user.management.repository.UserRepository;
import com.subash.user.management.util.GenericLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private GenericLogger genericLogger;

    @InjectMocks
    private UserServiceImpl userService;

    private final String uuid = "test-uuid";

    private final String username = "subi";
    private UserView userView;

    private User user;

    @BeforeEach
    void setup() {
        userView = new UserView();
        userView.setUsername(username);
        userView.setPassword("plainPassword");
        userView.setRole(UserView.RoleEnum.USER);
        userView.setEmailId("subi@gmail.com");
        userView.setPhoneNumber("8293738321");

        user = new User();
        user.setUsername(username);
        user.setPasswordHash("encryptPassword");
        user.setRole(Role.ROLE_USER);
        user.setEmailId("subi@gmail.com");
        user.setPhoneNumber("8293738321");
    }

    @Test
    void testCreateUser_whenUserDoesNotExist_shouldCreateUser() throws Exception {

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainPassword")).thenReturn("encryptPassword");
        when(userRepository.save(any())).thenReturn(user);

        ResponseEntity<UserResponse> response = userService.createUser(uuid, userView);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(5001, response.getBody().getCode());
    }

    @Test
    void testCreateUser_whenUserExists_shouldReturnExistCode() throws Exception {

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        ResponseEntity<UserResponse> response = userService.createUser(uuid, userView);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5002, response.getBody().getCode());
    }

    @Test
    void testGetUser_whenUserExists_shouldReturnUser() throws Exception {
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        ResponseEntity<UserResponse> response = userService.getUser(uuid, username);

        assertEquals(5004, response.getBody().getCode());
        assertNotNull(response.getBody().getUser());
    }

    @Test
    void testGetUser_whenNotFound_shouldReturnNotFoundCode() throws Exception {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        ResponseEntity<UserResponse> response = userService.getUser(uuid, username);

        assertEquals(5003, response.getBody().getCode());
        assertNull(response.getBody().getUser());
    }

    @Test
    void testGetAllUser_shouldReturnList() throws Exception {
        when(userRepository.findAll()).thenReturn(List.of(user));
        ResponseEntity<AllUserResponse> response = userService.getAllUser(uuid);

        assertEquals(5004, response.getBody().getCode());
        assertEquals(1, response.getBody().getUsers().size());
    }

    @Test
    void testRemoveUser_whenExists_shouldDelete() throws Exception {
        User user = new User();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        ResponseEntity<UserResponse> response = userService.removeUser(uuid, username);

        assertEquals(5005, response.getBody().getCode());
        verify(userRepository).delete(user);
    }

    @Test
    void testRemoveUser_whenNotFound_shouldReturnNotFoundCode() throws Exception {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        ResponseEntity<UserResponse> response = userService.removeUser(uuid, username);

        assertEquals(5003, response.getBody().getCode());
        verify(userRepository, never()).delete(any());
    }

    @Test
    void testCreateUser_shouldHandleException() {

        when(userRepository.findByUsername(anyString())).thenThrow(new RuntimeException("DB error"));

        assertThrows(Exception.class, () -> userService.createUser(uuid, userView));
        verify(genericLogger).logResponse(any(), eq(uuid), eq("ERROR"), any());
    }
}
