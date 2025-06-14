package com.subash.user.management.controller;

import com.subash.user.management.model.UserResponse;
import com.subash.user.management.model.UserView;
import com.subash.user.management.service.UserService;
import com.subash.user.management.service.UserServiceImpl;
import com.subash.user.management.util.Constants;
import com.subash.user.management.util.GenericLogger;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.subash.user.management.util.Constants.COMMA;
import static com.subash.user.management.util.Constants.LOG_MESSAGE;


@RestController
@RequestMapping("/v1/api")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);
    private UserService userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    /**
     * @param userView (required)
     * @return
     * @throws Exception
     */
    @PostMapping("/users/register")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserView userView) throws Exception {
        String uuid = GenericLogger.getUUID();
        logger.info(uuid + COMMA + LOG_MESSAGE + "Request received to user registration");
        //Log request
        GenericLogger.logRequest(logger, uuid, Constants.CREATE_USER, Constants.POST_METHOD, userView);
        ResponseEntity<UserResponse> userResponse = userService.createUser(uuid, userView);
        //Log response
        GenericLogger.logResponse(logger, uuid, HttpStatus.CREATED.name(), userResponse);
        logger.info(uuid + COMMA + LOG_MESSAGE + "User registration request completed");
        return userResponse;
    }

    /**
     * @param username username to retrieve user details (required)
     * @return
     * @throws Exception
     */
    @GetMapping("/users/{username}")
    public ResponseEntity<UserResponse> getUser(@Valid @PathVariable("username") String username) throws Exception {
        String uuid = GenericLogger.getUUID();
        logger.info(uuid + COMMA + LOG_MESSAGE + "Request received to fetch user");
        //Log request
        GenericLogger.logRequest(logger, uuid, Constants.GET_USER, Constants.GET_METHOD, username);
        ResponseEntity<UserResponse> userResponse  = userService.getUser(uuid, username);
        //Log response
        GenericLogger.logResponse(logger, uuid, HttpStatus.CREATED.name(), userResponse);
        logger.info(uuid + COMMA + LOG_MESSAGE + "Fetch user request completed");
        return userResponse;
    }
}
