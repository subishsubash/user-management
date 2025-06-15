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

/**
 * Unit tests for {@link com.subash.user.management.service.UserServiceImpl}.
 * <p>
 * This test class uses {@link MockitoExtension} to enable mocking and injection of dependencies
 * for isolated unit testing. It tests the service logic without involving any Spring context or web layer.
 * </p>
 *
 * <p>Mocked Dependencies:</p>
 * <ul>
 *     <li>{@link UserRepository} - for user data persistence</li>
 *     <li>{@link PasswordEncoder} - for encoding user passwords</li>
 *     <li>{@link GenericLogger} - for logging structured response data</li>
 * </ul>
 *
 * <p>
 * Tests cover all positive and negative paths, including:
 * <ul>
 *   <li>Creating new users</li>
 *   <li>Handling existing users</li>
 *   <li>Fetching users</li>
 *   <li>Deleting users</li>
 *   <li>Exception handling</li>
 * </ul>
 * </p>
 */
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

    /**
     * Initializes test data before each test.
     * Sets up a default {@link UserView} and {@link User} instance.
     */
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

    /**
     * Test to verify that a new user is created successfully
     * when the username does not already exist.
     */
    @Test
    void testCreateUser_whenUserDoesNotExist_shouldCreateUser() throws Exception {

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainPassword")).thenReturn("encryptPassword");
        when(userRepository.save(any())).thenReturn(user);

        ResponseEntity<UserResponse> response = userService.createUser(uuid, userView);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(5001, response.getBody().getCode());
    }

    /**
     * Test to verify that when a user already exists with the given username,
     * the service returns an appropriate response without creating a new user.
     */
    @Test
    void testCreateUser_whenUserExists_shouldReturnExistCode() throws Exception {

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        ResponseEntity<UserResponse> response = userService.createUser(uuid, userView);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5002, response.getBody().getCode());
    }

    /**
     * Test to verify successful retrieval of an existing user by username.
     */
    @Test
    void testGetUser_whenUserExists_shouldReturnUser() throws Exception {
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        ResponseEntity<UserResponse> response = userService.getUser(uuid, username);

        assertEquals(5004, response.getBody().getCode());
        assertNotNull(response.getBody().getUser());
    }

    /**
     * Test to verify that when a username is not found,
     * the service responds with a "not found" code and no user data.
     */
    @Test
    void testGetUser_whenNotFound_shouldReturnNotFoundCode() throws Exception {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        ResponseEntity<UserResponse> response = userService.getUser(uuid, username);

        assertEquals(5003, response.getBody().getCode());
        assertNull(response.getBody().getUser());
    }

    /**
     * Test to verify that all users are fetched correctly
     * from the repository.
     */
    @Test
    void testGetAllUser_shouldReturnList() throws Exception {
        when(userRepository.findAll()).thenReturn(List.of(user));
        ResponseEntity<AllUserResponse> response = userService.getAllUser(uuid);

        assertEquals(5004, response.getBody().getCode());
        assertEquals(1, response.getBody().getUsers().size());
    }

    /**
     * Test to verify successful deletion of an existing user.
     */
    @Test
    void testRemoveUser_whenExists_shouldDelete() throws Exception {
        User user = new User();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        ResponseEntity<UserResponse> response = userService.removeUser(uuid, username);

        assertEquals(5005, response.getBody().getCode());
        verify(userRepository).delete(user);
    }

    /**
     * Test to verify that attempting to delete a non-existing user
     * returns the appropriate not-found response and avoids repository delete call.
     */
    @Test
    void testRemoveUser_whenNotFound_shouldReturnNotFoundCode() throws Exception {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        ResponseEntity<UserResponse> response = userService.removeUser(uuid, username);

        assertEquals(5003, response.getBody().getCode());
        verify(userRepository, never()).delete(any());
    }


    /**
     * Test to verify that exceptions thrown during user creation
     * are handled and logged appropriately.
     */
    @Test
    void testCreateUser_shouldHandleException() {

        when(userRepository.findByUsername(anyString())).thenThrow(new RuntimeException("DB error"));

        assertThrows(Exception.class, () -> userService.createUser(uuid, userView));
        verify(genericLogger).logResponse(any(), eq(uuid), eq("ERROR"), any());
    }
}
