package com.subash.user.management.service;

import com.subash.user.management.mapper.UserMapper;
import com.subash.user.management.model.User;
import com.subash.user.management.model.UserResponse;
import com.subash.user.management.model.UserView;
import com.subash.user.management.repository.UserRepository;
import com.subash.user.management.util.Constants;
import com.subash.user.management.util.GenericLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import static com.subash.user.management.util.Constants.*;

/**
 * Implementation class for UserService
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    /**
     * createUser
     *
     * @param userView
     * @return
     */
    @Override
    public UserResponse createUser(String uuid, UserView userView) throws Exception {
        logger.info(uuid + COMMA + LOG_MESSAGE + "Processing create user request");
        UserResponse userResponse = new UserResponse();
        try {
            User user = userRepository.save(UserMapper.INSTANCE.userViewToUser(userView));
            userResponse.setUser(UserMapper.INSTANCE.userToUserView(user));
            userResponse.setCode(SUCCESS_CODE);
            userResponse.setMessage(CREATE_RECORD_SUCCESS);
        } catch (Exception e) {
            // Logger error response
            GenericLogger.logResponse(logger, uuid, "ERROR", Constants.CREATE_RECORD_FAILURE);
            logger.error(Constants.API_PROCESSED_FAILURE + " : " + e);
            throw new Exception(e);
        }
        return userResponse;
    }

    /***
     *
     * @param uuid
     * @param userView
     * @return
     */
    @Override
    public UserResponse getUser(String uuid, UserView userView) {
        return null;
    }
}
