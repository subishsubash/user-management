package com.subash.user.management.service;

import com.subash.user.management.model.AllUserResponse;
import com.subash.user.management.model.UserResponse;
import com.subash.user.management.model.UserView;
import org.springframework.http.ResponseEntity;

/**
 * Service interface for user-related operations.
 * <p>
 * This interface defines the contract for creating, retrieving, listing, and deleting user accounts.
 * Each method accepts a UUID string used for tracing/logging purposes.
 */
public interface UserService {
    /**
     * Creates a new user based on the provided user view model.
     *
     * @param uuid     unique identifier for logging/tracing
     * @param userView the user details to be registered
     * @return a {@link ResponseEntity} containing {@link UserResponse} with status and result
     * @throws Exception in case of validation, persistence, or processing errors
     */
    ResponseEntity<UserResponse> createUser(String uuid, UserView userView) throws Exception;

    /**
     * Retrieves details of a specific user by username.
     *
     * @param uuid     unique identifier for logging/tracing
     * @param userName the username of the user to retrieve
     * @return a {@link ResponseEntity} containing {@link UserResponse} with user data
     * @throws Exception if user is not found or any error occurs
     */
    ResponseEntity<UserResponse> getUser(String uuid, String userName) throws Exception;

    /**
     * Retrieves all users in the system.
     *
     * @param uuid unique identifier for logging/tracing
     * @return a {@link ResponseEntity} containing {@link AllUserResponse} with the user list
     * @throws Exception if fetching users fails
     */
    ResponseEntity<AllUserResponse> getAllUser(String uuid) throws Exception;

    /**
     * Removes a user from the system by username.
     *
     * @param uuid     unique identifier for logging/tracing
     * @param userName the username of the user to delete
     * @return a {@link ResponseEntity} containing {@link UserResponse} with the result
     * @throws Exception if user cannot be deleted or not found
     */
    ResponseEntity<UserResponse> removeUser(String uuid, String userName) throws Exception;
}
