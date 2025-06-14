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
 * Implementation class for UserService
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final GenericLogger genericLogger;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, GenericLogger genericLogger) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.genericLogger = genericLogger;

    }

    /**
     * Create User method
     *
     * @param uuid
     * @param userView
     * @return
     * @throws Exception
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
     * Get user method
     *
     * @param uuid
     * @param userName
     * @return
     * @throws Exception
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
     * Get All users
     *
     * @param uuid
     * @return
     * @throws Exception
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
     * Remove user
     *
     * @param uuid
     * @param userName
     * @return
     * @throws Exception
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
