package com.subash.user.management.controller;

import com.subash.user.management.model.UserResponse;
import com.subash.user.management.model.UserView;
import com.subash.user.management.service.UserService;
import com.subash.user.management.service.UserServiceImpl;
import com.subash.user.management.util.Constants;
import com.subash.user.management.util.GenericLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static com.subash.user.management.util.Constants.FAILURE_CODE;

/**
 * REST controller that implements the {@link UsersControllerApi} interface,
 * providing endpoints for user operations such as creation and retrieval.
 * <p>
 * This controller delegates business logic to {@link UserService}.
 */
@RestController
public class UserController implements UsersControllerApi {

    private static final Logger logger = LogManager.getLogger(UserController.class);
    private UserService userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    /**
     * Handles the HTTP POST request to create a new user.
     * <p>
     * Logs the request details using a UUID for traceability and delegates
     * the creation logic to {@link UserService#createUser(String, UserView)}.
     *
     * @param userView the request body containing user details to be created
     * @return {@link ResponseEntity} containing the created {@link UserResponse} and HTTP 201 (Created) status
     */
    @Override
    public ResponseEntity<UserResponse> createUser(UserView userView) {
        UserResponse userResponse = new UserResponse();
        try {
            String uuid = GenericLogger.getUUID();
            GenericLogger.logRequest(logger, uuid, Constants.CREATE_USER, Constants.POST_METHOD, userView);
            userResponse = userService.createUser(uuid, userView);
            return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            userResponse.setUser(null);
            userResponse.setCode(FAILURE_CODE);
            userResponse.setMessage(Constants.API_PROCESSED_FAILURE);
            return new ResponseEntity<>(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<UserResponse> getUser(Integer username) {
        return null;
    }
}
