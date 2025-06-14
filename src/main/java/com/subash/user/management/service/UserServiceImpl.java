package com.subash.user.management.service;

import com.subash.user.management.mapper.UserMapper;
import com.subash.user.management.model.AllUserResponse;
import com.subash.user.management.model.User;
import com.subash.user.management.model.UserResponse;
import com.subash.user.management.model.UserView;
import com.subash.user.management.repository.UserRepository;
import com.subash.user.management.util.Constants;
import com.subash.user.management.util.GenericLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.subash.user.management.util.Constants.*;

/**
 * Implementation class for {@link UserService} that provides user-related operations such as
 * creating, retrieving, listing, and removing users.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final GenericLogger genericLogger;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a new instance of {@code UserServiceImpl}.
     *
     * @param userRepository  repository for user persistence
     * @param passwordEncoder encoder for hashing user passwords
     * @param genericLogger   logger for structured logging
     */
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, GenericLogger genericLogger) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.genericLogger = genericLogger;

    }

    /**
     * Creates a new user after checking if the username already exists. If not, it saves the user with a hashed password.
     *
     * @param uuid     unique identifier for tracing/logging
     * @param userView user data received from the client
     * @return a response containing created user data or an existing record message
     * @throws Exception if there is a failure during processing
     */
    @Override
    public ResponseEntity<UserResponse> createUser(String uuid, UserView userView) throws Exception {
        logger.info(uuid + COMMA + LOG_MESSAGE + "Processing create user request");
        UserResponse userResponse = new UserResponse();
        try {

            Optional<User> userOptional = userRepository.findByUsername(userView.getUsername());
            if (userOptional.isPresent()) {
                userResponse.setCode(RECORD_EXIST_CODE);
                userResponse.setMessage(RECORD_EXIST);
                return new ResponseEntity<>(userResponse, HttpStatus.OK);
            } else {
                User user = UserMapper.INSTANCE.userViewToUser(userView);
                // Hash password before storing
                user.setPasswordHash(passwordEncoder.encode(userView.getPassword()));
                userResponse.setUser(UserMapper.INSTANCE.userToUserView(userRepository.save(user)));
                userResponse.setCode(CREATE_RECORD_SUCCESS_CODE);
                userResponse.setMessage(CREATE_RECORD_SUCCESS);
            }
        } catch (Exception e) {
            // Logger error response
            genericLogger.logResponse(logger, uuid, "ERROR", Constants.API_PROCESSED_FAILURE);
            throw new Exception(e);
        }
        logger.info(uuid + COMMA + LOG_MESSAGE + "Create user request processed");
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }


    /**
     * Retrieves a user by their username.
     *
     * @param uuid     unique identifier for tracing/logging
     * @param userName username to look up
     * @return a response containing the user data if found, or a not found message
     * @throws Exception if an error occurs during retrieval
     */
    @Override
    public ResponseEntity<UserResponse> getUser(String uuid, String userName) throws Exception {
        logger.info(uuid + COMMA + LOG_MESSAGE + "Processing get user request");
        UserResponse userResponse = new UserResponse();
        try {

            Optional<User> userOptional = userRepository.findByUsername(userName);
            if (userOptional.isPresent()) {
                userResponse.setUser(UserMapper.INSTANCE.userToUserView(userOptional.get()));
                userResponse.setCode(RECORD_FOUND_CODE);
                userResponse.setMessage(RECORD_FOUND);
            } else {
                userResponse.setUser(null);
                userResponse.setCode(RECORD_NOT_FOUND_CODE);
                userResponse.setMessage(RECORD_NOT_FOUND);
            }

        } catch (Exception e) {
            // Logger error response
            genericLogger.logResponse(logger, uuid, "ERROR", Constants.API_PROCESSED_FAILURE);
            throw new Exception(e);
        }
        logger.info(uuid + COMMA + LOG_MESSAGE + "Get user request processed");
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    /**
     * Retrieves all users in the system.
     *
     * @param uuid unique identifier for tracing/logging
     * @return a response containing a list of all users
     * @throws Exception if an error occurs during data retrieval
     */
    @Override
    public ResponseEntity<AllUserResponse> getAllUser(String uuid) throws Exception {
        logger.info(uuid + COMMA + LOG_MESSAGE + "Processing get All user request");
        AllUserResponse allUserResponse = new AllUserResponse();
        try {
            List<UserView> userViewList = UserMapper.INSTANCE.userListToUserViewList(userRepository.findAll());
            userViewList.forEach(userView -> {
                allUserResponse.addUsersItem(userView);
            });
            allUserResponse.setCode(RECORD_FOUND_CODE);
            allUserResponse.setMessage(RECORD_FOUND);

        } catch (Exception e) {
            // Logger error response
            genericLogger.logResponse(logger, uuid, "ERROR", Constants.API_PROCESSED_FAILURE);
            throw new Exception(e);
        }
        logger.info(uuid + COMMA + LOG_MESSAGE + "Get All user request processed");
        return new ResponseEntity<>(allUserResponse, HttpStatus.OK);
    }

    /**
     * Deletes a user by username.
     *
     * @param uuid     unique identifier for tracing/logging
     * @param userName username of the user to be deleted
     * @return a response indicating the result of the delete operation
     * @throws Exception if deletion fails
     */
    @Override
    public ResponseEntity<UserResponse> removeUser(String uuid, String userName) throws Exception {
        logger.info(uuid + COMMA + LOG_MESSAGE + "Processing remove user request");
        UserResponse userResponse = new UserResponse();
        try {

            Optional<User> userOptional = userRepository.findByUsername(userName);
            if (userOptional.isPresent()) {
                userRepository.delete(userOptional.get());
                userResponse.setCode(RECORD_REMOVED_CODE);
                userResponse.setMessage(RECORD_REMOVED);
                return new ResponseEntity<>(userResponse, HttpStatus.OK);
            } else {
                userResponse.setCode(RECORD_NOT_FOUND_CODE);
                userResponse.setMessage(RECORD_NOT_FOUND);
            }
        } catch (Exception e) {
            // Logger error response
            genericLogger.logResponse(logger, uuid, "ERROR", Constants.API_PROCESSED_FAILURE);
            throw new Exception(e);
        }
        logger.info(uuid + COMMA + LOG_MESSAGE + "Remove user request processed");
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
