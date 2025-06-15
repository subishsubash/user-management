package com.subash.user.management.controller;

import com.subash.user.management.model.AllUserResponse;
import com.subash.user.management.model.UserResponse;
import com.subash.user.management.model.UserView;
import com.subash.user.management.service.UserService;
import com.subash.user.management.util.Constants;
import com.subash.user.management.util.GenericLogger;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static com.subash.user.management.util.Constants.*;


/**
 * REST controller for managing user operations like registration, fetching user details,
 * fetching all users, and removing users.
 */
@RestController
@RequestMapping("/v1/api")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);
    private final UserService userService;
    private final GenericLogger genericLogger;

    /**
     * Constructs a UserController with required dependencies.
     *
     * @param userService     service to handle user-related operations
     * @param genericLogger   logger utility for structured logging
     */
    public UserController(UserService userService, GenericLogger genericLogger) {

        this.userService = userService;
        this.genericLogger = genericLogger;
    }

    /**
     * Endpoint to register a new user.
     *
     * @param userView the user details to be registered
     * @return ResponseEntity containing user registration response
     * @throws Exception if any exception occurs during registration
     */
    @PostMapping("/users/register")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserView userView) throws Exception {
        String uuid = GenericLogger.getUUID();
        logger.info(uuid + COMMA + LOG_MESSAGE + "Request received to user registration");
        //Log request
        genericLogger.logRequest(logger, uuid, Constants.CREATE_USER, Constants.POST_METHOD, userView);
        ResponseEntity<UserResponse> userResponse = userService.createUser(uuid, userView);
        //Log response
        genericLogger.logResponse(logger, uuid, HttpStatus.OK.name(), userResponse);
        logger.info(uuid + COMMA + LOG_MESSAGE + "User registration request completed");
        return userResponse;
    }

    /**
     * Endpoint to fetch a specific user's details by username.
     * Only the authenticated user or an admin can access this.
     *
     * @param username the username of the user to retrieve
     * @return ResponseEntity containing user details
     * @throws Exception if the user is not authorized or retrieval fails
     */
    @GetMapping("/users/{username}")
    public ResponseEntity<UserResponse> getUser(@Valid @PathVariable("username") String username) throws Exception {
        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Collection<? extends GrantedAuthority> roles =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        Boolean isAdminRole = roles.stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        if (!authenticatedUsername.equals(username) && !isAdminRole) {
            UserResponse userResponse = new UserResponse();
            userResponse.setMessage(ACCESS_DENIED);
            userResponse.setCode(ACCESS_DENIED_CODE);
            return new ResponseEntity<>(userResponse, HttpStatus.FORBIDDEN);
        }

        String uuid = GenericLogger.getUUID();
        logger.info(uuid + COMMA + LOG_MESSAGE + "Request received to fetch user");
        //Log request
        genericLogger.logRequest(logger, uuid, Constants.GET_USER, Constants.GET_METHOD, username);
        ResponseEntity<UserResponse> userResponse = userService.getUser(uuid, username);
        //Log response
        genericLogger.logResponse(logger, uuid, HttpStatus.OK.name(), userResponse);
        logger.info(uuid + COMMA + LOG_MESSAGE + "Fetch user request completed");
        return userResponse;
    }


    /**
     * Endpoint to fetch all users in the system.
     *
     * @return ResponseEntity containing all user details
     * @throws Exception if fetching fails
     */
    @GetMapping("/users")
    public ResponseEntity<AllUserResponse> getUser() throws Exception {
        String uuid = GenericLogger.getUUID();
        logger.info(uuid + COMMA + LOG_MESSAGE + "Request received to fetch all user");
        //Log request
        genericLogger.logRequest(logger, uuid, Constants.GET_ALL_USER, Constants.GET_METHOD, null);
        ResponseEntity<AllUserResponse> allUserResponse = userService.getAllUser(uuid);
        //Log response
        genericLogger.logResponse(logger, uuid, HttpStatus.OK.name(), allUserResponse);
        logger.info(uuid + COMMA + LOG_MESSAGE + "Fetch all user request completed");
        return allUserResponse;
    }


    /**
     * Endpoint to remove a user by username.
     *
     * @param username the username of the user to be removed
     * @return ResponseEntity indicating the result of the deletion operation
     * @throws Exception if removal fails
     */
    @DeleteMapping("/users/{username}")
    public ResponseEntity<UserResponse> removeUser(@Valid @PathVariable("username") String username) throws Exception {
        String uuid = GenericLogger.getUUID();
        logger.info(uuid + COMMA + LOG_MESSAGE + "Request received to remove user");
        //Log request
        genericLogger.logRequest(logger, uuid, Constants.REMOVE_USER, Constants.DELETE_METHOD, username);
        ResponseEntity<UserResponse> userResponse = userService.removeUser(uuid, username);
        //Log response
        genericLogger.logResponse(logger, uuid, HttpStatus.OK.name(), userResponse);
        logger.info(uuid + COMMA + LOG_MESSAGE + "Remove user request completed");
        return userResponse;
    }

}
